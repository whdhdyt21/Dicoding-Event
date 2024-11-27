package com.wahid.dicodingevent.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.repository.SearchEventsRepository
import com.wahid.dicodingevent.ui.utils.Result

class SearchActivityViewModel(private val listEventsRepository: SearchEventsRepository) : ViewModel() {

    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun searchEvents(keyword: String): LiveData<Result<List<ListEventsItem>>> {
        listEvents = listEventsRepository.searchEvents(keyword)
        return listEvents
    }
}