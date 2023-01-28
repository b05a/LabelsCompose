package com.example.labelscompose.Repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.labelscompose.Clases.ColorLabel
import com.example.labelscompose.Clases.HeightLabel
import com.example.labelscompose.Clases.Label

@Database(entities = [Label::class, HeightLabel::class, ColorLabel::class], version = 1)
abstract class MainDb: RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        fun getDb(context: Context):MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "LabelsDb.db"
            ).build()
        }
    }
}