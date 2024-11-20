package com.example.sleepezdreamdiary.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import com.example.sleepezdreamdiary.data.database.SleepEZDatabase
import com.example.sleepezdreamdiary.service.AlarmService
import com.example.sleepezdreamdiary.utils.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm triggered")
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        var soundUriString = intent.getStringExtra("ALARM_SOUND_URI")

        Log.d("AlarmReceiver", "Received alarmId: $alarmId")
        Log.d("AlarmReceiver", "Received soundUriString: $soundUriString")

        if (soundUriString == null) {
            Log.e("AlarmReceiver", "soundUriString is null. Using default sound.")
            soundUriString = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()
        }

        val soundUri = Uri.parse(soundUriString)

        // Start AlarmService to play the sound
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("ALARM_SOUND_URI", soundUri.toString())
            putExtra("ALARM_ID", alarmId)
        }
        context.startForegroundService(serviceIntent)
        // Reschedule the alarm for the next day
        rescheduleAlarm(context, alarmId, soundUriString)

    }


    private fun rescheduleAlarm(context: Context, alarmId: Int, soundUriString: String?) {
        // Calculate the next trigger time (same time next day)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, get(Calendar.MINUTE))
        }
        val nextTriggerTime = calendar.timeInMillis

        // Reschedule the alarm
        AlarmScheduler.scheduleAlarm(
            context,
            alarmId,
            nextTriggerTime,
            soundUriString
        )

        // Update the alarm in the database
        updateAlarmTimeInDatabase(context, alarmId, nextTriggerTime)
    }

    private fun updateAlarmTimeInDatabase(context: Context, alarmId: Int, newTime: Long) {
        // Get the database
        val db = SleepEZDatabase.getDatabase(context)
        val alarmDao = db.alarmDao()

        // Run database operations on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            val alarm = alarmDao.getAlarmById(alarmId).firstOrNull()
            if (alarm != null) {
                val updatedAlarm = alarm.copy(time = newTime)
                alarmDao.updateAlarm(updatedAlarm)
            }
        }
    }
}