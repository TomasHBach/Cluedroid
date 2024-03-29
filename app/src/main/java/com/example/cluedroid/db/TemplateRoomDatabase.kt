package com.example.cluedroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cluedroid.dao.ActiveTemplateDao
import com.example.cluedroid.dao.TemplateDao
import com.example.cluedroid.dao.UserSettingsDao
import com.example.cluedroid.model.ActiveTemplate
import com.example.cluedroid.model.Template
import com.example.cluedroid.model.UserSettings

@Database(entities = [(Template::class), (ActiveTemplate::class), (UserSettings::class)], version = 1, exportSchema = false)
abstract class TemplateRoomDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
    abstract fun activeTemplateDao(): ActiveTemplateDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {

        @Volatile
        private var INSTANCE: TemplateRoomDatabase? = null

        fun getInstance(context: Context): TemplateRoomDatabase {
            synchronized(this) {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TemplateRoomDatabase::class.java,
                        "template_database"
                    ).createFromAsset("database/default_data.db")
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }

}