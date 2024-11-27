package com.wahid.dicodingevent.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.databinding.FragmentFavoriteBinding
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.data.local.entity.FavoriteEvent

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getFavoriteEvents()
        }

        viewModel.favoriteEvent.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.isNullOrEmpty()) {
                binding.errorPage.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.errorPage.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE
                setupRecyclerView(result)
            }
        }
    }

    private fun setupRecyclerView(favoriteEvents: List<FavoriteEvent>) {
        val listEventsItems = favoriteEvents.map {
            ListEventsItem(
                id = it.id,
                name = it.name,
                summary = it.summary,
                imageLogo = it.imageLogo ?: "",
                mediaCover = it.mediaCover ?: "",
                registrants = 0,
                link = "",
                description = "",
                ownerName = "",
                cityName = "",
                quota = 0,
                beginTime = "",
                endTime = "",
                category = ""
            )
        }
        val adapter = ListEventAdapter(listEventsItems)
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFavorite.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Retry fetching favorite events on button click
        binding.btnTryAgain.setOnClickListener {
            viewModel.getFavoriteEvents()
            binding.errorPage.visibility = View.GONE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
