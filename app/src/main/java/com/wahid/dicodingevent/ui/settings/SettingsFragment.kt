package com.wahid.dicodingevent.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.*
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.databinding.FragmentSettingsBinding
import com.wahid.dicodingevent.ui.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var workManager: WorkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        workManager = WorkManager.getInstance(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupSwitchListeners()
    }

    private fun setupObservers() {
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            updateTheme(isDarkModeActive)
        }

        viewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            binding.switchDailyReminder.isChecked = isReminderActive
        }
    }

    private fun setupSwitchListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            handleDailyReminder(isChecked)
        }
    }

    private fun updateTheme(isDarkModeActive: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        binding.switchDarkMode.isChecked = isDarkModeActive
    }

    private fun handleDailyReminder(isEnabled: Boolean) {
        if (isEnabled) {
            startDailyReminderTask()
        } else {
            stopDailyReminderTask()
        }
    }

    private fun startDailyReminderTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(DailyReminderWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(WORKER_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORKER_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
        viewModel.saveReminderSetting(true)
    }

    private fun stopDailyReminderTask() {
        workManager.cancelUniqueWork(WORKER_TAG)
        viewModel.saveReminderSetting(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val WORKER_TAG = "DAILY_REMINDER"
    }
}
