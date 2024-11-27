package com.wahid.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.repository.FinishedEventsRepository
import com.wahid.dicodingevent.ui.utils.Result

class FinishedViewModel(private val finishedEventsRepository: FinishedEventsRepository) : ViewModel() {

    val listEvents: LiveData<Result<List<ListEventsItem>>> = finishedEventsRepository.getFinishedEvent(5)

    fun getFinishedEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        return finishedEventsRepository.getFinishedEvent(limit)
    }
}
