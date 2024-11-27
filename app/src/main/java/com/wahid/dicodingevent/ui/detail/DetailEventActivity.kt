package com.wahid.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wahid.dicodingevent.R
import com.wahid.dicodingevent.data.local.entity.FavoriteEvent
import com.wahid.dicodingevent.databinding.ActivityDetailBinding
import com.wahid.dicodingevent.ViewModelFactory
import com.wahid.dicodingevent.data.model.Event
import com.wahid.dicodingevent.ui.utils.Result
import com.wahid.dicodingevent.ui.utils.loadImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailEventViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Event"
        }

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        if (savedInstanceState == null) {
            viewModel.getDetailEvent(eventId)
        }

        viewModel.event.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.detailRegister.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.detailRegister.visibility = View.VISIBLE
                    val eventData = result.data
                    supportActionBar?.title = eventData.name
                    populateEventDetails(eventData)
                    observeFavoriteStatus(eventData)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.detailRegister.visibility = View.GONE
                    binding.errorPage.visibility = if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.errorMessage.text = result.error
                }
            }
        }

        binding.detailRegister.setOnClickListener {
            val url = "https://www.dicoding.com/events/${eventId}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.getDetailEvent(eventId)
            binding.errorPage.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun populateEventDetails(eventData: Event) {
        binding.apply {
            detailBg.loadImage(eventData.mediaCover)
            detailName.text = eventData.name
            detailOwnerName.text = getString(R.string.organizer, eventData.ownerName)
            detailTime.text = getString(R.string.time, convertToHumanReadable(eventData.beginTime))
            detailQuota.text = getString(R.string.quota_available, String.format(Locale.getDefault(), "%d", eventData.quota - eventData.registrants))
            detailDesc.text = HtmlCompat.fromHtml(eventData.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            detailFabFavorite.visibility = View.VISIBLE
            detailRegister.visibility = View.VISIBLE
            errorPage.visibility = View.GONE
        }
    }

    private fun observeFavoriteStatus(eventData: Event) {
        viewModel.getFavoriteEventById(eventData.id).observe(this) { favoriteEvent ->
            Log.d("DetailEventActivity", "favoriteEvent: $favoriteEvent")
            val favoriteEventData = FavoriteEvent(
                eventData.id,
                eventData.name,
                eventData.mediaCover,
                eventData.imageLogo,
                eventData.summary
            )

            if (favoriteEvent != null) {
                binding.detailFabFavorite.setImageResource(R.drawable.ic_favorite)
                binding.detailFabFavorite.setOnClickListener {
                    viewModel.delete(favoriteEventData)
                }
            } else {
                binding.detailFabFavorite.setImageResource(R.drawable.ic_favorite_border)
                binding.detailFabFavorite.setOnClickListener {
                    viewModel.insert(favoriteEventData)
                }
            }
        }
    }

    private fun convertToHumanReadable(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val humanReadableFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("id", "ID"))
        return dateTime.format(humanReadableFormatter)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
