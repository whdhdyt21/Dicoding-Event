package com.wahid.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinishedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        setupRetryButton()

        if (savedInstanceState == null) {
            viewModel.getEvents()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun setupRetryButton() {
        binding.btnTryAgain.setOnClickListener {
            viewModel.getEvents()
            hideError()
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            listEvent.observe(viewLifecycleOwner) { events ->
                updateRecyclerView(events)
                hideError()
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.isVisible = isLoading
            }

            errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    showError(errorMessage)
                } else {
                    hideError()
                }
            }
        }
    }

    private fun updateRecyclerView(events: List<com.wahid.dicodingevent.data.model.ListEventsItem>) {
        binding.rvFinished.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinished.adapter = ListEventAdapter(events)
    }

    private fun showError(message: String) {
        binding.errorPage.visibility = View.VISIBLE
        binding.errorMessage.text = message
    }

    private fun hideError() {
        binding.errorPage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}