package com.example.cluedroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "template")
data class Template(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "suspects")
    var suspects:String,

    @ColumnInfo(name = "weapons")
    var weapons:String,

    @ColumnInfo(name = "rooms")
    var rooms:String,
)
