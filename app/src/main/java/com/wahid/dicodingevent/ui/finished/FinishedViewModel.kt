package com.wahid.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahid.dicodingevent.data.model.EventResponse
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val TAG = "FinishedViewModel"

    fun getEvents(limit: Int = 40) {
        _isLoading.value = true
        ApiConfig.getApiService().getFinishedEvents(limit).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _listEvent.value = events
                    _errorMessage.value = if (events.isEmpty()) "No finished events available." else ""
                    Log.d(TAG, "Successfully fetched finished events: ${events.size} items")
                } else {
                    handleError(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                handleError(t.message ?: "Unknown error occurred")
            }
        })
    }

    private fun handleError(message: String) {
        _errorMessage.value = message
        Log.e(TAG, "Error fetching finished events: $message")
    }
}