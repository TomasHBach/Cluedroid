package com.example.cluedroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class TemplateIdName(
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name:String,
)
