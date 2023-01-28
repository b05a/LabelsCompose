package com.example.labelscompose.Repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao
import com.example.labelscompose.Clases.*

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLabel(label: Label)

    @Query("SELECT * FROM Labels")
    fun getLabels():List<Label>

    @Delete
    fun deleteLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeightLabel(heightLabel: HeightLabel)

    @Query("SELECT * FROM HeightLabel")
    fun getHeightLabel():List<HeightLabel>

    @Delete
    fun deleteHeightLabel(label: HeightLabel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun changeColorLabel(color: ColorLabel)

    @Query("SELECT * FROM ColorLabel")
    fun getColorLabel():List<ColorLabel>
}