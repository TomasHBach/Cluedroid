package com.example.cluedroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "active_template")
data class ActiveTemplate (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "active_template_index")
    var activeTemplateIndex:String,

    @ColumnInfo(name = "suspects_booleans")
    var suspectsBooleans:String,

    @ColumnInfo(name = "weapons_booleans")
    var weaponsBooleans:String,

    @ColumnInfo(name = "rooms_booleans")
    var roomsBooleans:String
)
