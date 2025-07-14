package com.example.aiexpenzo.datastore


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.datastore by preferencesDataStore(name ="settings")
object ReminderPrefsKeys {
    val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
}

class ReminderPreferences(private val context: Context){
    val reminderEnabledFlow: Flow<Boolean> = context.datastore.data
        .map{ prefs -> prefs[ReminderPrefsKeys.REMINDER_ENABLED] ?: false}

    suspend fun setReminderEnabled(enabled: Boolean){
        context.datastore.edit{prefs ->
            prefs[ReminderPrefsKeys.REMINDER_ENABLED] =enabled
        }
    }
}