package com.example.sleepezdreamdiary.presentation.ui.add_alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Alarm
import com.example.sleepezdreamdiary.utils.ValidationUtils
import java.text.DateFormat
import java.util.Date

@Composable
fun AddAlarmScreen(onSave: (Alarm) -> Unit) {
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isActive by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Placeholder for Time Picker
        Text("Time: ${DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(time))}")
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Active")
            Switch(
                checked = isActive,
                onCheckedChange = { isActive = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (ValidationUtils.validateAlarm(time)) {
                    val alarm = Alarm(
                        time = time, isActive = isActive
                    )
                    onSave(alarm)
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Alarm")
        }
    }
}
