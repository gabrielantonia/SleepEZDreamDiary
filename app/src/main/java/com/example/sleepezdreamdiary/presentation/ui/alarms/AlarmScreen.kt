package com.example.sleepezdreamdiary.presentation.ui.alarms

import android.app.TimePickerDialog
import android.media.RingtoneManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Alarm
import com.example.sleepezdreamdiary.presentation.ui.components.AlarmItem
import com.example.sleepezdreamdiary.presentation.viewmodel.AlarmViewModel
import com.example.sleepezdreamdiary.utils.sendTestNotification
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(alarmViewModel: AlarmViewModel) {
    val alarms by alarmViewModel.alarms.collectAsState()
    val context = LocalContext.current
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Use a Box to overlay the FAB over the content
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // TopAppBar at the top
            TopAppBar(
                title = { Text("Alarms") },
                actions = {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Send Test Notification") },
                            onClick = {
                                isMenuExpanded = false
                                sendTestNotification(context)
                            }
                        )
                        // Add more menu items as needed
                    }
                }
            )

            // Content area
            if (alarms.isEmpty()) {
                Text(
                    "No alarms set",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(alarms) { alarm ->
                        AlarmItem(alarm = alarm, alarmViewModel = alarmViewModel)
                    }
                }
            }
        }

        // FloatingActionButton at the bottom right
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.surface,
            onClick = {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }

                        // If the selected time is before the current time, add one day
                        if (calendar.timeInMillis <= System.currentTimeMillis()) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                        }

                        // Set a default sound URI
                        val defaultSoundUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()

                        alarmViewModel.addAlarm(
                            Alarm(
                                time = calendar.timeInMillis,
                                isActive = true,
                                soundUri = defaultSoundUri // Set default sound
                            )
                        )
                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    true
                )

                timePickerDialog.show()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Alarm")
        }
    }
}