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

class UpcomingEventsRepository private constructor(private val apiService: ApiService) {

    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()

    fun getUpcomingEvents(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        result.value = Result.Loading
        val client = apiService.getUpcomingEvents(limit)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!.listEvents)
                } else {
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    companion object {
        @Volatile
        private var instance: UpcomingEventsRepository? = null

        fun getInstance(apiService: ApiService): UpcomingEventsRepository {
            return instance ?: synchronized(this) {
                instance ?: UpcomingEventsRepository(apiService).also { instance = it }
            }
        }
    }
}