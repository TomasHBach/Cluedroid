package com.example.cluedroid.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.cluedroid.model.UserSettings

@Dao
interface UserSettingsDao {

    @Query("SELECT * FROM user_settings WHERE id = 0")
    fun getUserSettings(): UserSettings

    @Query("SELECT app_theme FROM user_settings WHERE id = 0")
    fun getTheme(): String

    @Query("UPDATE user_settings SET app_theme = :theme WHERE id = 0")
    fun updateTheme(theme: String)

    @Update
    suspend fun updateUserSettings(userSettings: UserSettings)

}