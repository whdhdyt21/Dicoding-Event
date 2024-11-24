package com.wahid.dicodingevent.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.R
import com.wahid.dicodingevent.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchActivityViewModel by viewModels()

    companion object {
        const val EXTRA_SEARCH = "extra_search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupRecyclerView()
        setupSearchBar()
        setupObservers()

        val keyword = intent.getStringExtra(EXTRA_SEARCH).orEmpty()
        if (savedInstanceState == null && keyword.isNotBlank()) {
            viewModel.searchEvent(keyword)
        }

        binding.btnTryAgain.setOnClickListener {
            retrySearch()
        }

        applyWindowInsets()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.search_event)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = ListEventAdapter(emptyList())
    }

    private fun setupSearchBar() {
        with(binding) {
            searchBar.setText(intent.getStringExtra(EXTRA_SEARCH))
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                errorPage.visibility = View.GONE
                searchBar.setText(searchView.text)
                searchView.hide()
                rvSearch.adapter = null // Clear previous results
                viewModel.searchEvent(searchBar.text.toString())
                false
            }
        }
    }

    private fun setupObservers() {
        viewModel.listEvent.observe(this) { events ->
            val adapter = ListEventAdapter(events)
            binding.rvSearch.adapter = adapter
            showError(false)
        }

        viewModel.resultMessage.observe(this) { resultText: String ->
            binding.searchNotFound.isVisible = resultText.isNotEmpty()
            binding.searchNotFound.text = resultText
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showError(true, errorMessage)
            } else {
                showError(false)
            }
        }
    }

    private fun retrySearch() {
        viewModel.searchEvent(binding.searchBar.text.toString())
        showError(false)
    }

    private fun showError(show: Boolean, message: String = "") {
        binding.errorPage.isVisible = show
        binding.errorMessage.text = message
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
