package com.wahid.dicodingevent.ui.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahid.dicodingevent.R
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.ListEventAdapter
import com.wahid.dicodingevent.databinding.ActivitySearchBinding
import com.wahid.dicodingevent.ui.utils.Result

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchActivityViewModel by viewModels {
        ViewModelFactory.getInstance(this@SearchActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Search Event"
        }

        val keyword = intent.getStringExtra(EXTRA_SEARCH) ?: ""

        if (savedInstanceState == null) {
            viewModel.searchEvents(keyword)
        }

        with(binding) {
            searchBar.setText(intent.getStringExtra(EXTRA_SEARCH))
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                errorPage.visibility = View.GONE
                searchBar.setText(searchView.text)
                searchView.hide()
                rvSearch.adapter = null
                viewModel.searchEvents(searchBar.text.toString())
                false
            }
        }

        viewModel.listEvents.observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val listEventData = result.data
                    binding.rvSearch.layoutManager = LinearLayoutManager(this@SearchActivity)
                    binding.rvSearch.adapter = ListEventAdapter(listEventData)
                    binding.errorPage.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorPage.visibility = if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.errorMessage.text = result.error
                }
            }
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.searchEvents(binding.searchBar.text.toString())
            binding.errorPage.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onNavigateUp()
    }

    companion object {
        const val EXTRA_SEARCH = "extra_search"
    }
}