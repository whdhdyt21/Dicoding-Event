package com.wahid.dicodingevent

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wahid.dicodingevent.databinding.ActivityMainBinding
import com.wahid.dicodingevent.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_upcoming,
                R.id.navigation_finished
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_btn -> {
                showSearchView()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSearchView() {
        binding.searchView.show()
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString().trim()
            if (query.isNotEmpty()) {
                navigateToSearchActivity(query)
            } else {
                Toast.makeText(this, getString(R.string.search_empty_warning), Toast.LENGTH_SHORT).show()
            }
            binding.searchView.hide()
            false
        }
    }

    private fun navigateToSearchActivity(query: String) {
        Intent(this, SearchActivity::class.java).apply {
            putExtra(SearchActivity.EXTRA_SEARCH, query)
            startActivity(this)
        }
    }
}
