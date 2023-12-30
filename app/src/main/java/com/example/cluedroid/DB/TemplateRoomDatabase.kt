package com.example.cluedroid.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cluedroid.dao.TemplateDao
import com.example.cluedroid.model.Template

@Database(entities = [(Template::class)], version = 1, exportSchema = true)
abstract class TemplateRoomDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao

    companion object {

        @Volatile
        private var INSTANCE: TemplateRoomDatabase? = null

        fun getInstance(context: Context): TemplateRoomDatabase {
            synchronized(this) {
                return INSTANCE?: synchronized(this) {
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