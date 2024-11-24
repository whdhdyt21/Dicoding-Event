package com.wahid.dicodingevent.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels()

    private val eventAdapter: ListEventAdapter by lazy { ListEventAdapter(emptyList()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("UpcomingFragment", "onViewCreated: savedInstanceState=$savedInstanceState")

        setupRecyclerView()
        setupObservers()

        if (savedInstanceState == null) {
            viewModel.getEvents(40) // Initial fetch with a limit
        }

        binding.btnTryAgain.setOnClickListener {
            retryFetchingEvents()
        }
    }

    private fun setupRecyclerView() {
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }
    }

    private fun setupObservers() {
        viewModel.listEvent.observe(viewLifecycleOwner) { events ->
            eventAdapter.updateEvents(events)
            showError(false)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showError(true, errorMessage)
            } else {
                showError(false)
            }
        }
    }

    private fun retryFetchingEvents() {
        viewModel.getEvents()
        showError(false)
    }

    private fun showError(show: Boolean, message: String = "") {
        binding.errorPage.isVisible = show
        binding.errorMessage.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
