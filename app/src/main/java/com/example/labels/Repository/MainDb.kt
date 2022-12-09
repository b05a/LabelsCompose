package com.example.labels.Repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.labels.Clases.ColorLabel
import com.example.labels.Clases.HeightLabel
import com.example.labels.Clases.Label

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