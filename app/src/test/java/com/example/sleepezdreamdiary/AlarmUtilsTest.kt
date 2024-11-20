package com.example.sleepezdreamdiary

import org.junit.Assert.*
import org.junit.Test
import java.util.*
import kotlin.apply

class AlarmUtilsTest {

    @Test
    fun getNextAlarmTime_withFutureTime_returnsSameDay() {
        val now = Calendar.getInstance()
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + 1)
        }.timeInMillis

        val nextAlarmTime = AlarmUtils.getNextAlarmTime(alarmTime, now.timeInMillis)
        val expectedTime = Calendar.getInstance().apply {
            timeInMillis = alarmTime
        }.timeInMillis

        assertEquals(expectedTime, nextAlarmTime)
    }

    @Test
    fun getNextAlarmTime_withPastTime_returnsNextDay() {
        val now = Calendar.getInstance()
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) - 1)
        }.timeInMillis

        val nextAlarmTime = AlarmUtils.getNextAlarmTime(alarmTime, now.timeInMillis)
        val expectedTime = Calendar.getInstance().apply {
            timeInMillis = alarmTime
            add(Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis

        assertEquals(expectedTime, nextAlarmTime)
    }
}

object AlarmUtils {
    fun getNextAlarmTime(alarmTime: Long, currentTime: Long = System.currentTimeMillis()): Long {
        val alarmCalendar = Calendar.getInstance().apply {
            timeInMillis = alarmTime
        }

        val nowCalendar = Calendar.getInstance().apply {
            timeInMillis = currentTime
        }

        // Set the date of alarmCalendar to today
        alarmCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR))
        alarmCalendar.set(Calendar.DAY_OF_YEAR, nowCalendar.get(Calendar.DAY_OF_YEAR))

        if (alarmCalendar.timeInMillis <= nowCalendar.timeInMillis) {
            // If the time is in the past or now, add one day
            alarmCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return alarmCalendar.timeInMillis
    }
}