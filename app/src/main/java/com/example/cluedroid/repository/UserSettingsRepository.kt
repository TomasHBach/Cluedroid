package com.example.cluedroid.repository

import com.example.cluedroid.dao.UserSettingsDao
import com.example.cluedroid.model.UserSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserSettingsRepository(private val userSettingsDao: UserSettingsDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getUserSettings(): UserSettings {
        return userSettingsDao.getUserSettings()
    }

    fun getTheme(): String {
        return userSettingsDao.getTheme()
    }

    fun updateTheme(theme: String) {
        coroutineScope.launch(Dispatchers.IO) {
            userSettingsDao.updateTheme(theme)
        }
    }

    fun updateUserSettings(userSettings: UserSettings) {
        coroutineScope.launch(Dispatchers.IO) {
            userSettingsDao.updateUserSettings(userSettings)
        }
    }

}