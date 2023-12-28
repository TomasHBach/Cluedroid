package com.example.cluedroid.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cluedroid.dao.TemplateDao
import com.example.cluedroid.model.Template
import kotlinx.coroutines.newSingleThreadContext

@Database(entities = [(Template::class)], version = 1, exportSchema = false)
abstract class TemplateRoomDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao

    companion object {

        @Volatile
        private var INSTANCE: TemplateRoomDatabase? = null

        suspend fun getInstance(context: Context): TemplateRoomDatabase? {
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TemplateRoomDatabase::class.java,
                    "template_database"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                INSTANCE!!.populateInitialData()

            }
            return INSTANCE
        }
    }

    suspend fun populateInitialData() {
        if (templateDao().getAllTemplates().size == 0) {
            templateDao().addTemplate(template = Template(1, "Titulo 1", "asd", "asd", "asdas"))
        }
    }

}