package com.example.labels.Clases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Labels")
data class Label (
    @PrimaryKey
    var id: Int,
    var name:String,
    var vertical:Int,
    var horizontal:Int,
    var verticalVal:Boolean
    )

@Entity(tableName = "HeightLabel")
data class HeightLabel(
    @PrimaryKey
    var id: Int,
    var name: String,
    var height: Int,
    var horizontalVal:Boolean
)

@Entity(tableName = "ColorLabel")
data class ColorLabel(
    @PrimaryKey
    var id: String = "color",
    var color:String = "green"
)