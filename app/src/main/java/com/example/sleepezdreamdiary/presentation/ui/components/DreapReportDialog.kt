package com.example.sleepezdreamdiary.presentation.ui.components

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun DreamReportDialog(onDismiss: () -> Unit, onGenerateReport: (Long, Long, Uri) -> Unit) {
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }
    var context = LocalContext.current
    // Create a launcher for the folder picker intent
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            selectedFolderUri = it
        }
    }
    context
    AlertDialog(onDismissRequest = { onDismiss() }, title = { Text("Dream Report") }, text = {
        Column {
            Button(onClick = {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)
                        startDate = calendar.timeInMillis
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }) {
                Text("Select Start Date")
            }

            startDate?.let {
                Text("Start Date: ${DateFormat.getDateInstance().format(Date(it))}")
            }

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)
                        endDate = calendar.timeInMillis
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }) {
                Text("Select End Date")
            }

            endDate?.let {
                Text("End Date: ${DateFormat.getDateInstance().format(Date(it))}")
            }

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                folderPickerLauncher.launch(null)
            }) {
                Text("Select Folder")
            }

            selectedFolderUri?.let {
                Text("Selected Folder: $it")
            }
        }
    }, confirmButton = {
        if (startDate != null && endDate != null && selectedFolderUri != null) {
            TextButton(onClick = {
                onGenerateReport(startDate!!, endDate!!, selectedFolderUri!!)
            }) {
                Text("Generate Report")
            }
        }
    }, dismissButton = {
        TextButton(onClick = { onDismiss() }) {
            Text("Cancel")
        }
    })
}