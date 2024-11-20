package com.example.sleepezdreamdiary.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat


fun sendTestNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "test_channel"

    // Create the notification channel (for Android O and above)
    val channel = NotificationChannel(
        channelId, "Test Notifications", NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // Use a built-in icon
        .setContentTitle("Test Notification").setContentText("This is a test notification.")
        .setPriority(NotificationCompat.PRIORITY_HIGH).build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1001, notification)
        } else {
            // Handle lack of permission
            Toast.makeText(context, "Notification permission not granted.", Toast.LENGTH_LONG)
                .show()
            // Optionally, guide the user to app settings
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    } else {
        // For older Android versions, permission is granted at install time
        notificationManager.notify(1001, notification)
    }
}