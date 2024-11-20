package com.example.sleepezdreamdiary.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sleepezdreamdiary.data.dao.AlarmDao
import com.example.sleepezdreamdiary.data.dao.DreamDao
import com.example.sleepezdreamdiary.data.model.Alarm
import com.example.sleepezdreamdiary.data.model.Dream

@Database(entities = [Dream::class, Alarm::class], version = 1, exportSchema = false)
abstract class SleepEZDatabase : RoomDatabase() {
    abstract fun dreamDao(): DreamDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: SleepEZDatabase? = null

        fun getDatabase(context: Context): SleepEZDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SleepEZDatabase::class.java,
                    "sleepez_database"
                )
                    .fallbackToDestructiveMigration() // Reset database on version mismatch
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}