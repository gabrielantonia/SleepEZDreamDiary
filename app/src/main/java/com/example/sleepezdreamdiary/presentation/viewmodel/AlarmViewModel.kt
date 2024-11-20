package com.example.sleepezdreamdiary.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sleepezdreamdiary.data.model.Alarm
import com.example.sleepezdreamdiary.data.repository.AlarmRepository
import com.example.sleepezdreamdiary.utils.AlarmScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AlarmViewModel(
    application: Application,
    private val alarmRepository: AlarmRepository
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    // Check for exact alarm permission and prompt user to enable it in app settings
    private fun ensureExactAlarmPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Show a message and open app-specific settings to request permission
                Toast.makeText(
                    context,
                    "Please enable exact alarm permission for this app to set alarms.",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                return false
            }
        }
        return true
    }

    // Flow to observe all alarms
    val alarms: StateFlow<List<Alarm>> =
        alarmRepository.getAllAlarms().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Add this new StateFlow for active alarms
    val activeAlarms: StateFlow<List<Alarm>> =
        alarmRepository.getActiveAlarms()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Function to add a new alarm and schedule it, only if permission is granted
    fun addAlarm(alarm: Alarm) {
        if (ensureExactAlarmPermission()) {
            viewModelScope.launch {
                // Adjust the time before inserting
                val triggerTime = getNextAlarmTime(alarm.time)
                val adjustedAlarm = alarm.copy(time = triggerTime)

                val newAlarmId = alarmRepository.insertAlarm(adjustedAlarm).toInt()

                if (alarm.isActive) {
                    AlarmScheduler.scheduleAlarm(
                        context = context,
                        alarmId = newAlarmId,
                        triggerTime = triggerTime,
                        soundUri = adjustedAlarm.soundUri
                    )
                }
            }
        }
    }

    // Function to update an existing alarm and reschedule if active, only if permission is granted
    fun updateAlarm(alarm: Alarm) {
        if (ensureExactAlarmPermission()) { // Only proceed if permission is granted
            viewModelScope.launch {
                if (alarm.isActive) {
                    val triggerTime = getNextAlarmTime(alarm.time)
                    val updatedAlarm = alarm.copy(time = triggerTime)
                    alarmRepository.updateAlarm(updatedAlarm)
                    AlarmScheduler.scheduleAlarm(
                        context = context,
                        alarmId = updatedAlarm.id,
                        triggerTime = triggerTime,
                        soundUri = updatedAlarm.soundUri // Pass soundUri here
                    )
                } else {
                    alarmRepository.updateAlarm(alarm)
                    AlarmScheduler.cancelAlarm(context, alarm.id)
                }
            }
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(alarm)
            AlarmScheduler.cancelAlarm(context, alarm.id)
        }
    }

    private fun getNextAlarmTime(alarmTime: Long): Long {
        val alarmCalendar = Calendar.getInstance().apply {
            timeInMillis = alarmTime
        }

        val nowCalendar = Calendar.getInstance()

        // Set the date of alarmCalendar to today
        alarmCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR))
        alarmCalendar.set(Calendar.DAY_OF_YEAR, nowCalendar.get(Calendar.DAY_OF_YEAR))

        if (alarmCalendar.timeInMillis <= nowCalendar.timeInMillis) {
            // If the time is in the past or now, add one day
            alarmCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return alarmCalendar.timeInMillis
    }


    // Function to update soundUri for a specific alarm
    fun updateAlarmSoundUri(alarmId: Int, soundUri: String) {
        viewModelScope.launch {
            // Update the soundUri in the database
            alarmRepository.updateAlarmSoundUri(alarmId, soundUri)

            // Get the updated alarm from the database
            val updatedAlarm = alarmRepository.getAlarmByIdSync(alarmId)

            if (updatedAlarm != null && updatedAlarm.isActive) {
                // Cancel the existing alarm
                AlarmScheduler.cancelAlarm(context, alarmId)

                // Reschedule the alarm with the updated soundUri
                AlarmScheduler.scheduleAlarm(
                    context = context,
                    alarmId = alarmId,
                    triggerTime = updatedAlarm.time,
                    soundUri = soundUri
                )
            }
        }
    }
}

class AlarmViewModelFactory(
    private val application: Application, private val alarmRepository: AlarmRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(application, alarmRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}