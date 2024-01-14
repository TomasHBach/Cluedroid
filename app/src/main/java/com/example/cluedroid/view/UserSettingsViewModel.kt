package com.example.cluedroid.view

import androidx.lifecycle.ViewModel
import com.example.cluedroid.model.UserSettings
import com.example.cluedroid.repository.UserSettingsRepository
import javax.inject.Inject

class UserSettingsViewModel @Inject constructor(private val userSettingsRepository: UserSettingsRepository) :
    ViewModel() {

    fun getUserSettings(): UserSettings {
        return userSettingsRepository.getUserSettings()
    }

    fun getTheme(): String {
        return userSettingsRepository.getTheme()
    }

    fun updateTheme(theme: String) {
        userSettingsRepository.updateTheme(theme)
    }

    fun updateUserSettings(userSettings: UserSettings) {
        userSettingsRepository.updateUserSettings(userSettings)
    }

}