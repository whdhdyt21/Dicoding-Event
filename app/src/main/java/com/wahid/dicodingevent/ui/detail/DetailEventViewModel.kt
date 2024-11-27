package com.wahid.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.dicodingevent.data.local.entity.FavoriteEvent
import com.wahid.dicodingevent.data.model.Event
import com.wahid.dicodingevent.repository.EventRepository
import com.wahid.dicodingevent.repository.FavoriteEventRepository
import com.wahid.dicodingevent.ui.utils.Result
import kotlinx.coroutines.launch

class DetailEventViewModel(
    private val eventRepository: EventRepository,
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModel() {
    lateinit var event: LiveData<Result<Event>>
    lateinit var favoriteEvent: LiveData<FavoriteEvent>

    fun getDetailEvent(id: Int): LiveData<Result<Event>> {
        event = eventRepository.getDetailEvent(id)
        return event
    }

    fun insert(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.insert(favoriteEvent)
        }
    }

    fun getFavoriteEventById(id: Int): LiveData<FavoriteEvent> {
        favoriteEvent = favoriteEventRepository.getFavoriteEventById(id)
        return favoriteEvent
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.delete(favoriteEvent)
        }
    }
}