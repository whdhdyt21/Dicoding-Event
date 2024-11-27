package com.wahid.dicodingevent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.wahid.dicodingevent.data.model.DetailEventResponse
import com.wahid.dicodingevent.data.model.Event
import com.wahid.dicodingevent.data.network.ApiService
import com.wahid.dicodingevent.ui.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(private val apiService: ApiService) {

    private val result = MediatorLiveData<Result<Event>>()


    fun getDetailEvent(id: Int): LiveData<Result<Event>> {
        result.value = Result.Loading
        val client = apiService.getEventDetails(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!.event)
                } else {
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService): EventRepository {
            return instance ?: synchronized(this) {
                instance ?: EventRepository(apiService).also { instance = it }
            }
        }
    }
}