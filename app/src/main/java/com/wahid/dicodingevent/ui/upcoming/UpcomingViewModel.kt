package com.wahid.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.repository.UpcomingEventsRepository
import com.wahid.dicodingevent.ui.utils.Result

class UpcomingViewModel(private val upcomingEventsRepository: UpcomingEventsRepository) : ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun getUpcomingEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        listEvents = upcomingEventsRepository.getUpcomingEvents(limit)
        return listEvents
    }
}