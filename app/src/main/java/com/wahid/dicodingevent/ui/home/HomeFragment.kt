package com.wahid.dicodingevent.ui.home

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
import com.wahid.dicodingevent.databinding.FragmentHomeBinding
import com.wahid.dicodingevent.ui.finished.FinishedViewModel
import com.wahid.dicodingevent.ui.upcoming.UpcomingViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel: UpcomingViewModel by viewModels()
    private val finishedViewModel: FinishedViewModel by viewModels()

    private val eventLimit = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fetchEvents()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRetryButtons()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFinishedEvents()
        observeUpcomingEvents()
    }

    private fun fetchEvents() {
        finishedViewModel.getEvents(eventLimit)
        upcomingViewModel.getEvents(eventLimit)
    }

    private fun setupRetryButtons() {
        binding.btnTryAgain.setOnClickListener {
            upcomingViewModel.getEvents(eventLimit)
            hideError(binding.upcomingErrorPage)
        }
        binding.finishedBtnTryAgain.setOnClickListener {
            finishedViewModel.getEvents(eventLimit)
            hideError(binding.finishedErrorPage)
        }
    }

    private fun observeFinishedEvents() {
        finishedViewModel.apply {
            listEvent.observe(viewLifecycleOwner) { events ->
                updateRecyclerView(
                    recyclerView = binding.rvFinished,
                    events = events,
                    layoutManager = LinearLayoutManager(requireActivity())
                )
                hideError(binding.finishedErrorPage)
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.finishedProgressBar.isVisible = it
            }

            errorMessage.observe(viewLifecycleOwner) {
                displayError(binding.finishedErrorPage, binding.finishedErrorMessage, it)
            }
        }
    }

    private fun observeUpcomingEvents() {
        upcomingViewModel.apply {
            listEvent.observe(viewLifecycleOwner) { events ->
                updateRecyclerView(
                    recyclerView = binding.rvUpcoming,
                    events = events,
                    layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ),
                    isHorizontal = true
                )
                hideError(binding.upcomingErrorPage)
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.upcomingProgressBar.isVisible = it
            }

            errorMessage.observe(viewLifecycleOwner) {
                displayError(binding.upcomingErrorPage, binding.errorMessage, it)
            }
        }
    }

    private fun updateRecyclerView(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        events: List<com.wahid.dicodingevent.data.model.ListEventsItem>,
        layoutManager: LinearLayoutManager,
        isHorizontal: Boolean = false
    ) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ListEventAdapter(events, horizontal = isHorizontal)
    }

    private fun displayError(errorPage: View, errorMessageView: android.widget.TextView, message: String) {
        if (message.isNotEmpty()) {
            errorPage.visibility = View.VISIBLE
            errorMessageView.text = message
        } else {
            errorPage.visibility = View.GONE
        }
    }

    private fun hideError(errorPage: View) {
        errorPage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}