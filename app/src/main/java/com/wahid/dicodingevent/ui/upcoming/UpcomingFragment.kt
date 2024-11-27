package com.wahid.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.databinding.FragmentUpcomingBinding
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.ui.utils.Result

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        setupRetryButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) fetchUpcomingEvents()
        observeViewModel()
    }

    private fun fetchUpcomingEvents() {
        viewModel.getUpcomingEvent(40)
    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> showEvents(result.data)
                is Result.Error -> showError(result.error)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            errorPage.visibility = View.GONE
        }
    }

    private fun showEvents(events: List<ListEventsItem>) {
        binding.apply {
            progressBar.visibility = View.GONE
            errorPage.visibility = View.GONE
            setupRecyclerView(events)
        }
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            errorPage.visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
            btnTryAgain.visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
            rvUpcoming.visibility = if (errorMessage.isNotEmpty()) View.GONE else View.VISIBLE
            binding.errorMessage.text = errorMessage
        }
    }

    private fun setupRecyclerView(events: List<ListEventsItem>) {
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ListEventAdapter(events)
        }
    }

    private fun setupRetryButton() {
        binding.btnTryAgain.setOnClickListener {
            fetchUpcomingEvents()
            binding.errorPage.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}