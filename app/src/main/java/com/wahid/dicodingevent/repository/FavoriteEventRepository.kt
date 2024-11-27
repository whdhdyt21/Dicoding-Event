package com.wahid.dicodingevent.repository

import android.content.Context
import com.wahid.dicodingevent.data.local.entity.FavoriteEvent
import com.wahid.dicodingevent.data.local.entity.FavoriteEventDAO
import com.wahid.dicodingevent.data.local.room.FavoriteEventRoomDatabase

class FavoriteEventRepository(context: Context) {
    private val mFavoriteEventDao: FavoriteEventDAO

    init {
        val db = FavoriteEventRoomDatabase.getDatabase(context)
        mFavoriteEventDao = db.favoriteEventDao()
    }

    suspend fun insert(favoriteEvent: FavoriteEvent) {
        mFavoriteEventDao.insert(favoriteEvent)
    }

    fun getFavoriteEventById(id: Int) = mFavoriteEventDao.getFavoriteEventById(id)

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        mFavoriteEventDao.delete(favoriteEvent)
    }

    fun getFavoriteEvents() = mFavoriteEventDao.getFavoriteEvents()
}
