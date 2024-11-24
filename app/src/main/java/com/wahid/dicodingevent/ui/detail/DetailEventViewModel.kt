package com.wahid.dicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahid.dicodingevent.data.model.DetailEventResponse
import com.wahid.dicodingevent.data.model.Event
import com.wahid.dicodingevent.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getDetailEvent(id: Int) {
        if (id < 0) {
            _errorMessage.value = "Invalid event ID"
            return
        }

        _isLoading.value = true
        ApiConfig.getApiService().getEventDetails(id).enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _event.value = response.body()?.event
                    _errorMessage.value = if (_event.value == null) "No event details available." else ""
                } else {
                    handleError(response.message() ?: "Unknown error occurred")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                handleError(t.message ?: "Network failure occurred")
            }
        })
    }

    private fun handleError(message: String) {
        Log.e(TAG, message)
        _errorMessage.value = message
    }

    companion object {
        private const val TAG = "DetailActivityViewModel"
    }
}
