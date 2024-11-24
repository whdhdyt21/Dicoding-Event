package com.wahid.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.wahid.dicodingevent.R
import com.wahid.dicodingevent.databinding.ActivityDetailBinding
import com.wahid.dicodingevent.ui.utils.loadImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailEventViewModel by viewModels()

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

        viewModel.event.observe(this) { event ->
            supportActionBar?.title = event.name

            binding.apply {
                detailBg.loadImage(event.mediaCover)
                detailName.text = event.name
                detailOwnerName.text = getString(R.string.organizer, event.ownerName)
                detailTime.text = getString(R.string.time, convertToHumanReadable(event.beginTime))
                detailQuota.text = getString(
                    R.string.quota_available,
                    String.format(Locale.getDefault(), "%d", event.quota - event.registrants)
                )
                detailDesc.text = HtmlCompat.fromHtml(
                    event.description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                detailRegister.visibility = View.VISIBLE
                errorPage.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        binding.detailRegister.setOnClickListener {
            val url = "https://www.dicoding.com/events/$eventId"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            binding.apply {
                errorPage.visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
                binding.errorMessage.text = errorMessage
            }
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.getDetailEvent(eventId)
            binding.errorPage.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertToHumanReadable(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val humanReadableFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm 'WIB'", Locale("id", "ID"))
        return dateTime.format(humanReadableFormatter)
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
