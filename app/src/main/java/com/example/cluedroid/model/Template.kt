package com.example.cluedroid.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NonNls

@Entity (tableName = "template")
data class Template(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "suspects")
    var suspects:String,

    @ColumnInfo(name = "weapons")
    var weapons:String,

    @ColumnInfo(name = "rooms")
    var rooms:String,
)
