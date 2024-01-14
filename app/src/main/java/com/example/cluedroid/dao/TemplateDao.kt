package com.example.cluedroid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cluedroid.model.Template

@Dao
interface TemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTemplate(template: Template)

    @Query("SELECT * FROM template WHERE id = :templateId")
    fun findTemplateById(templateId: String): Template

    @Query("SELECT * FROM template")
    fun getAllTemplates(): List<Template>

    @Update
    suspend fun updateTemplate(template: Template)

    @Delete
    suspend fun deleteTemplate(template: Template)

}