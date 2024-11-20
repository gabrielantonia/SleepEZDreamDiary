package com.example.sleepezdreamdiary.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sleepezdreamdiary.receiver.DismissAlarmReceiver


class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AlarmService", "AlarmService started")
        val soundUriString = intent?.getStringExtra("ALARM_SOUND_URI") ?: return START_NOT_STICKY
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val soundUri = Uri.parse(soundUriString)

        // Set up MediaPlayer with the selected alarm sound
        mediaPlayer = MediaPlayer.create(this, soundUri).apply {
            isLooping = true  // Loop until dismissed
            start()
        }

        // Ensure the notification channel exists
        val channelId = "alarm_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId, "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for ongoing alarm notifications"
            setSound(null, null) // Disable notification sound
            enableVibration(false) // Disable vibration
        }
        notificationManager.createNotificationChannel(channel)

        // Dismiss action intent
        val dismissIntent = Intent(this, DismissAlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarmId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            alarmId,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Delete intent for when the notification is swiped away
        val deletePendingIntent = PendingIntent.getBroadcast(
            this, alarmId, dismissIntent, // Reuse the dismissIntent to stop the alarm
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set up the foreground notification
        val notification: Notification =
            NotificationCompat.Builder(this, channelId).setContentTitle("Alarm is ringing")
                .setContentText("Tap to dismiss")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(dismissPendingIntent)
                .setDeleteIntent(deletePendingIntent) // Handle swipe dismissal
                .setAutoCancel(false).setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).build()

        startForeground(alarmId, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("AlarmService", "AlarmService destroyed and mediaPlayer released")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("AlarmService", "onTaskRemoved called")

        // Stop the media player
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        // Stop the service
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}