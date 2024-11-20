package com.example.sleepezdreamdiary.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.sleepezdreamdiary.service.AlarmService


class DismissAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)

        Log.d("DismissAlarmReceiver", "Received dismiss for alarmId: $alarmId")

        // Stop AlarmService to halt the sound
        val stopIntent = Intent(context, AlarmService::class.java)
        context.stopService(stopIntent)

        // Cancel the alarm notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(alarmId)
    }
}