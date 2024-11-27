package com.wahid.dicodingevent

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wahid.dicodingevent.ui.dependency.Injection
import com.wahid.dicodingevent.ui.detail.DetailEventViewModel
import com.wahid.dicodingevent.ui.favorite.FavoriteViewModel
import com.wahid.dicodingevent.ui.finished.FinishedViewModel
import com.wahid.dicodingevent.ui.search.SearchActivityViewModel
import com.wahid.dicodingevent.ui.settings.SettingsPreferences
import com.wahid.dicodingevent.ui.settings.SettingsViewModel
import com.wahid.dicodingevent.ui.settings.dataStore
import com.wahid.dicodingevent.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(
    private val repositories: Map<Class<out ViewModel>, () -> ViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = repositories[modelClass]
            ?: throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        return creator.invoke() as? T
            ?: throw IllegalArgumentException("Could not cast ViewModel for: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    mapOf(
                        DetailEventViewModel::class.java to {
                            DetailEventViewModel(
                                Injection.provideRepository(context),
                                Injection.provideFavoriteEventRepository(context)
                            )
                        },
                        UpcomingViewModel::class.java to {
                            UpcomingViewModel(Injection.provideUpcomingEventsRepository(context))
                        },
                        FinishedViewModel::class.java to {
                            FinishedViewModel(Injection.provideFinishedEventsRepository(context))
                        },
                        SearchActivityViewModel::class.java to {
                            SearchActivityViewModel(Injection.provideSearchEventsRepository(context))
                        },
                        FavoriteViewModel::class.java to {
                            FavoriteViewModel(Injection.provideFavoriteEventRepository(context))
                        },
                        SettingsViewModel::class.java to {
                            SettingsViewModel(SettingsPreferences.getInstance(context.dataStore))
                        }
                    )
                ).also { instance = it }
            }
    }
}
