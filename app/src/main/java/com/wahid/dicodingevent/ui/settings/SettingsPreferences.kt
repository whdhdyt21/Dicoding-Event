package com.wahid.dicodingevent.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_SETTING_KEY = "theme_setting"
    private val REMINDER_SETTING_KEY = "reminder_setting"

    private val THEME_KEY = booleanPreferencesKey(THEME_SETTING_KEY)
    private val REMINDER_KEY = booleanPreferencesKey(REMINDER_SETTING_KEY)

    fun getThemeSetting(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[THEME_KEY] == true }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences -> preferences[THEME_KEY] = isDarkModeActive }
    }

    fun getReminderSetting(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[REMINDER_KEY] == true }

    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        dataStore.edit { preferences -> preferences[REMINDER_KEY] = isReminderActive }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsPreferences(dataStore).also { INSTANCE = it }
            }
        }
    }
}
