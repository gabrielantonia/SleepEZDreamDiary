package com.example.sleepezdreamdiary.presentation.ui.components

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Alarm
import com.example.sleepezdreamdiary.presentation.viewmodel.AlarmViewModel
import com.example.sleepezdreamdiary.utils.getSoundNameFromUri
import java.text.DateFormat
import java.util.Date

@Composable
fun AlarmItem(alarm: Alarm, alarmViewModel: AlarmViewModel) {
    val context = LocalContext.current
    var selectedSoundUri by remember { mutableStateOf(alarm.soundUri) }
    var selectedSoundName by remember { mutableStateOf("Pick Sound") }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Load the saved sound name when the composable loads
    LaunchedEffect(alarm.soundUri) {
        selectedSoundUri = alarm.soundUri
        selectedSoundName =
            selectedSoundUri?.let { getSoundNameFromUri(context, Uri.parse(it)) } ?: "Pick Sound"
    }

    // Remember launcher for activity result to pick a sound
    val ringtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? =
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            uri?.let {
                selectedSoundUri = it.toString()
                selectedSoundName = getSoundNameFromUri(context, it)

                // Persist the sound URI in the database via ViewModel
                alarmViewModel.updateAlarmSoundUri(alarm.id, selectedSoundUri!!)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(alarm.time)),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = alarm.isActive, onCheckedChange = { isChecked ->
                val updatedAlarm = alarm.copy(isActive = isChecked)
                alarmViewModel.updateAlarm(updatedAlarm)
            })
        }

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            // Button to pick a sound for the alarm
            Button(
                onClick = {
                    val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
                        putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound")
                        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                        putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                            selectedSoundUri?.let { Uri.parse(it) }
                                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                    }
                    ringtonePickerLauncher.launch(intent)
                }, modifier = Modifier.weight(1f)
            ) {
                Text("Sound: $selectedSoundName")
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Demo button: only visible when a sound is selected
            if (selectedSoundUri != null) {
                IconButton(
                    onClick = {
                        // Play the selected sound using MediaPlayer
                        mediaPlayer?.release() // Release any previous MediaPlayer instance
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(context, Uri.parse(selectedSoundUri))
                            prepare()
                            start()
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = com.example.sleepezdreamdiary.R.drawable.baseline_music_note_24),
                        contentDescription = "Play Sound",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Delete Alarm button below the sound picker row
        Button(
            onClick = {
                mediaPlayer?.release() // Release MediaPlayer if deleting the alarm
                alarmViewModel.deleteAlarm(alarm)
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Alarm")
        }
    }

    // Release MediaPlayer when the composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }
}