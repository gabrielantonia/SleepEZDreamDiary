package com.example.sleepezdreamdiary.data.repository

import com.example.sleepezdreamdiary.data.dao.AlarmDao
import com.example.sleepezdreamdiary.data.model.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {
    fun getAllAlarms(): Flow<List<Alarm>> = alarmDao.getAllAlarms()

    suspend fun insertAlarm(alarm: Alarm): Long = alarmDao.insertAlarm(alarm)
    suspend fun updateAlarm(alarm: Alarm) = alarmDao.updateAlarm(alarm)
    suspend fun deleteAlarm(alarm: Alarm) = alarmDao.deleteAlarm(alarm)

    suspend fun getAlarmByIdSync(alarmId: Int): Alarm? = alarmDao.getAlarmByIdSync(alarmId)

    fun getActiveAlarms(): Flow<List<Alarm>> = alarmDao.getActiveAlarms()

    suspend fun updateAlarmSoundUri(alarmId: Int, soundUri: String) {
        alarmDao.updateSoundUri(alarmId, soundUri)
    }
}