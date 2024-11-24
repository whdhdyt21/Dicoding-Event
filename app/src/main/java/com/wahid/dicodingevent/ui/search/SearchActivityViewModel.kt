package com.wahid.dicodingevent.ui.search

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

class SearchActivityViewModel : ViewModel() {

    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> get() = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String> get() = _resultMessage

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvents(keyword)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents.orEmpty()
                    _listEvent.value = events
                    clearError()

                    // Hasil kosong
                    _resultMessage.value = if (events.isEmpty()) {
                        "No search results found for \"$keyword\"."
                    } else {
                        ""
                    }
                } else {
                    handleError("Response error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                handleError("Network failure: ${t.message}")
            }
        })
    }

    private fun clearError() {
        _errorMessage.value = ""
    }

    private fun handleError(message: String) {
        _errorMessage.value = message
        _resultMessage.value = ""
        Log.e("SearchActivityViewModel", message)
    }


}
