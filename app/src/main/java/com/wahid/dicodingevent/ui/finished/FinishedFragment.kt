package com.wahid.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.databinding.FragmentFinishedBinding
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.ui.utils.Result

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("View binding is only valid between onCreateView and onDestroyView")

    private val viewModel: FinishedViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val eventLimit = 40

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getFinishedEvent(eventLimit)
        }

        observeFinishedEvents()
    }

    private fun observeFinishedEvents() {
        viewModel.listEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setupRecyclerView(result.data)
                    binding.errorPage.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorPage.visibility = View.VISIBLE
                    binding.errorMessage.text = result.error
                }
            }
        }
    }

    private fun setupRecyclerView(events: List<ListEventsItem>) {
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListEventAdapter(events)
        binding.rvFinished.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            viewModel.getFinishedEvent(eventLimit)
            binding.errorPage.visibility = View.GONE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
