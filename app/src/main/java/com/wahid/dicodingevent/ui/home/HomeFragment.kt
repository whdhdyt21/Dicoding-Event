package com.wahid.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wahid.dicodingevent.databinding.FragmentHomeBinding
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.ui.finished.FinishedViewModel
import com.wahid.dicodingevent.ui.settings.SettingsViewModel
import com.wahid.dicodingevent.ui.upcoming.UpcomingViewModel
import com.wahid.dicodingevent.ui.utils.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel: UpcomingViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val finishedViewModel: FinishedViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val eventLimit = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            finishedViewModel.getFinishedEvent(eventLimit)
            upcomingViewModel.getUpcomingEvent(eventLimit)
        }

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        observeEvent(finishedViewModel.listEvents, binding.rvFinished, binding.finishedProgressBar, binding.finishedErrorPage, binding.finishedErrorMessage)
        observeEvent(upcomingViewModel.listEvents, binding.rvUpcoming, binding.upcomingProgressBar, binding.upcomingErrorPage, binding.finishedErrorMessage, isUpcoming = true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            upcomingViewModel.getUpcomingEvent(eventLimit)
            binding.upcomingErrorPage.visibility = View.GONE
        }
        binding.finishedBtnTryAgain.setOnClickListener {
            finishedViewModel.getFinishedEvent(eventLimit)
            binding.finishedErrorPage.visibility = View.GONE
        }

        return root
    }

    private fun observeEvent(
        liveData: LiveData<Result<List<ListEventsItem>>>,
        recyclerView: RecyclerView,
        progressBar: ProgressBar,
        errorPage: View,
        errorMessage: TextView,
        isUpcoming: Boolean = false
    ) {
        liveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    errorPage.visibility = View.GONE
                }
                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    val listEventData = result.data
                    val adapter = ListEventAdapter(listEventData, horizontal = isUpcoming)
                    recyclerView.layoutManager = LinearLayoutManager(requireActivity(), if (isUpcoming) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL, false)
                    recyclerView.adapter = adapter
                    errorPage.visibility = View.GONE
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    errorPage.visibility = if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                    errorMessage.text = result.error
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
