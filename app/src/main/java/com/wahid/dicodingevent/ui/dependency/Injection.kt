package com.wahid.dicodingevent.ui.dependency

import android.content.Context
import com.wahid.dicodingevent.repository.SearchEventsRepository
import com.wahid.dicodingevent.data.network.ApiConfig
import com.wahid.dicodingevent.repository.EventRepository
import com.wahid.dicodingevent.repository.FavoriteEventRepository
import com.wahid.dicodingevent.repository.FinishedEventsRepository
import com.wahid.dicodingevent.repository.UpcomingEventsRepository

object Injection {

    private val apiService by lazy { ApiConfig.getApiService() }

    fun provideRepository(context: Context): EventRepository {
        return EventRepository.getInstance(apiService)
    }

    fun provideUpcomingEventsRepository(context: Context): UpcomingEventsRepository {
        return UpcomingEventsRepository.getInstance(apiService)
    }

    fun provideFinishedEventsRepository(context: Context): FinishedEventsRepository {
        return FinishedEventsRepository.getInstance(apiService)
    }

    fun provideSearchEventsRepository(context: Context): SearchEventsRepository {
        return SearchEventsRepository.getInstance(apiService)
    }

    fun provideFavoriteEventRepository(context: Context): FavoriteEventRepository {
        return FavoriteEventRepository(context)
    }
}
