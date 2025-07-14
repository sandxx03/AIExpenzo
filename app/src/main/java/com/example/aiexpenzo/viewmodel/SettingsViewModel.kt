package com.example.aiexpenzo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpenzo.datastore.ReminderPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application){
    private val reminderPrefs = ReminderPreferences(application)

    val reminderEnabled = reminderPrefs.reminderEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setReminder(enabled: Boolean){
        viewModelScope.launch {
            reminderPrefs.setReminderEnabled(enabled)
        }
    }
}