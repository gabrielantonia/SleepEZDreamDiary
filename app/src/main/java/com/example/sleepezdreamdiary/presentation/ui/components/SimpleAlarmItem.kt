package com.example.sleepezdreamdiary.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Alarm
import com.example.sleepezdreamdiary.presentation.viewmodel.AlarmViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun SimpleAlarmItem(alarm: Alarm, alarmViewModel: AlarmViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(alarm.time)),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = alarm.isActive, onCheckedChange = { isChecked ->
                val updatedAlarm = alarm.copy(isActive = isChecked)
                alarmViewModel.updateAlarm(updatedAlarm)
            }, colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
    }
}