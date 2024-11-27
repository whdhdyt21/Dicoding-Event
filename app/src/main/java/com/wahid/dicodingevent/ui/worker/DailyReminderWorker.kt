package com.wahid.dicodingevent.ui.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wahid.dicodingevent.R
import com.wahid.dicodingevent.data.model.EventResponse
import com.wahid.dicodingevent.data.network.ApiConfig
import retrofit2.Response

class DailyReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        private val TAG = DailyReminderWorker::class.java.simpleName
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Dicoding Channel"
    }

    override fun doWork(): Result {
        return try {
            val response = ApiConfig.getApiService().getOneUpcomingEvent().execute()
            if (response.isSuccessful) {
                handleSuccessfulResponse(response)
            } else {
                showNotification("No Events Available", response.message())
                Result.failure()
            }
        } catch (e: Exception) {
            showNotification("Failed to Fetch Event", e.message)
            Result.failure()
        }
    }

    private fun handleSuccessfulResponse(response: Response<EventResponse>): Result {
        val listEvents = response.body()?.listEvents
        val title = listEvents?.firstOrNull()?.name ?: "Upcoming Event"
        val time = listEvents?.firstOrNull()?.beginTime ?: "Unknown Time"
        showNotification(title, time)
        return Result.success()
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.event_upcoming)
            .setContentTitle(title)
            .setContentText(description ?: "Details unavailable")
            .setSubText("Upcoming Event")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        createNotificationChannel(notificationManager)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}
