package com.example.sleepezdreamdiary.data.dao

import androidx.room.*
import com.example.sleepezdreamdiary.data.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    fun getAlarmById(alarmId: Int): Flow<Alarm?>

    @Query("UPDATE alarms SET soundUri = :soundUri WHERE id = :alarmId")
    suspend fun updateSoundUri(alarmId: Int, soundUri: String)

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarmByIdSync(alarmId: Int): Alarm?

    @Query("SELECT * FROM alarms WHERE isActive = 1 ORDER BY time ASC")
    fun getActiveAlarms(): Flow<List<Alarm>>
}