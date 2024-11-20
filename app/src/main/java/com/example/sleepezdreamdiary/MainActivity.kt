package com.example.sleepezdreamdiary

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.sleepezdreamdiary.presentation.ui.SleepEZDreamDiaryApp
import com.example.sleepezdreamdiary.theme.SleepEZDreamDiaryTheme


class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestForegroundServicePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Foreground service permission granted.", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Foreground service permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission() // Request notification permissions on launch
        requestForegroundServicePermission() // Request foreground service permission on launch
        checkExactAlarmPermission() // Prompt for exact alarm permissions if needed
        createNotificationChannel() // Set up notification channel

        setContent {
            SleepEZDreamDiaryTheme() {
                SleepEZDreamDiaryApp()
            }
        }
    }

    /**
     * Creates a notification channel for alarm notifications.
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "alarm_channel", "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for alarm notifications"
        }
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Requests notification permissions for Android 13+.
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * Requests foreground service permission for media playback (Android 14+).
     */
    private fun requestForegroundServicePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestForegroundServicePermissionLauncher.launch(Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
        }
    }

    /**
     * Checks if the exact alarm permission is granted and prompts the user if needed (Android 12+).
     */
    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(
                    this,
                    "Please enable exact alarm permission for this app to set alarms.",
                    Toast.LENGTH_LONG
                ).show()

                // Open the exact alarm permission settings
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }
    }
}