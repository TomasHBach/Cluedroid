package com.example.cluedroid.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.cluedroid.model.ActiveTemplate

@Dao
interface ActiveTemplateDao {

    @Query("SELECT * FROM active_template WHERE active_template.id = 0")
    fun getActiveTemplateData(): ActiveTemplate

    @Query("UPDATE active_template SET suspects_booleans = :data WHERE id = 0")
    fun updateSuspectsBooleans(data: String)

    @Query("UPDATE active_template SET weapons_booleans = :data WHERE id = 0")
    fun updateWeaponsBooleans(data: String)

    @Query("UPDATE active_template SET rooms_booleans = :data WHERE id = 0")
    fun updateRoomsBooleans(data: String)

    @Update
    suspend fun updateActiveTemplate(activeTemplate: ActiveTemplate)
}