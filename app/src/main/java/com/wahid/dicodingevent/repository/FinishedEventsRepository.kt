package com.wahid.dicodingevent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.wahid.dicodingevent.data.model.EventResponse
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.data.network.ApiService
import com.wahid.dicodingevent.ui.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedEventsRepository private constructor(private val apiService: ApiService) {

    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()

    fun getFinishedEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        result.value = Result.Loading
        val client = apiService.getFinishedEvents(limit)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!.listEvents)
                } else {
                    result.value = Result.Error("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error("Failure: ${t.message}")
            }
        })

        return result
    }

    companion object {
        @Volatile
        private var instance: FinishedEventsRepository? = null

        fun getInstance(apiService: ApiService): FinishedEventsRepository {
            return instance ?: synchronized(this) {
                instance ?: FinishedEventsRepository(apiService).also { instance = it }
            }
        }
    }
}
