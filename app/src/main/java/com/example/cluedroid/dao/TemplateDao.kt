package com.example.cluedroid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cluedroid.model.Template
import com.example.cluedroid.model.TemplateIdName

@Dao
interface TemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTemplate(template: Template)

    @Query("SELECT * FROM template WHERE id = :templateId")
    fun findTemplateById(templateId: String): Template

    @Query("SELECT id, name FROM template")
    fun getAllTemplatesIdName(): List<TemplateIdName>

    @Query("SELECT id FROM template ORDER BY id DESC LIMIT 1")
    fun getLastIndex(): Int

    @Query("SELECT id FROM template ORDER BY id ASC LIMIT 1")
    fun getFirstIndex(): Int

    @Update
    suspend fun updateTemplate(template: Template)

    @Delete
    suspend fun deleteTemplate(template: Template)

}