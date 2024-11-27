package com.wahid.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wahid.dicodingevent.data.local.entity.FavoriteEvent
import com.wahid.dicodingevent.repository.FavoriteEventRepository

class FavoriteViewModel(private val favoriteEventRepository: FavoriteEventRepository) : ViewModel() {

    var favoriteEvent: LiveData<List<FavoriteEvent>> = favoriteEventRepository.getFavoriteEvents()

    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        favoriteEvent = favoriteEventRepository.getFavoriteEvents()
        return favoriteEvent
    }
}
