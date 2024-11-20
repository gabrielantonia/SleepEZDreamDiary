package com.example.sleepezdreamdiary.presentation.ui.add_dream

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Dream
import com.example.sleepezdreamdiary.presentation.viewmodel.LogViewModel
import com.example.sleepezdreamdiary.utils.ValidationUtils

@Composable
fun AddDreamScreen(
    existingDreamId: Long,
    logViewModel: LogViewModel,
    onSave: (Dream) -> Unit,
    onDelete: (Dream) -> Unit // Add this parameter for deletion
) {
    val dream by logViewModel.getDreamById(existingDreamId).collectAsState(initial = null)

    var title by remember { mutableStateOf(dream?.title ?: "") }
    var content by remember { mutableStateOf(dream?.content ?: "") }
    var category by remember { mutableStateOf(dream?.category ?: "Vivid") }
    val categories = listOf("Vivid", "Nightmare", "Lucid", "Other")

    // State to manage the dropdown menu's expanded state
    var expanded by remember { mutableStateOf(false) }

    // State to manage the confirmation dialog visibility
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(dream) {
        // Update the state when the existing dream changes
        dream?.let {
            title = it.title
            content = it.content
            category = it.category
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Title Field with Character Limit
        TextField(
            value = title,
            onValueChange = {
                if (it.length <= 50) {
                    title = it
                }
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = title.length == 50
        )
        if (title.length == 50) {
            Text(
                text = "Character limit reached",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.height(8.dp))

        // Content Field with Character Limit
        TextField(
            value = content,
            onValueChange = {
                if (it.length <= 500) {
                    content = it
                }
            },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth(),
            isError = content.length == 500
        )
        if (content.length == 500) {
            Text(
                text = "Character limit reached",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.height(8.dp))

        // Dropdown Menu for selecting dream category
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.clickable { expanded = true }
                    )
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            category = cat
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Save or Update Dream Button
        Button(
            onClick = {
                if (ValidationUtils.validateDream(title, content)) {
                    val dreamToSave = dream?.copy(
                        title = title,
                        content = content,
                        category = category,
                        date = dream?.date ?: System.currentTimeMillis()
                    ) ?: Dream(
                        title = title,
                        content = content,
                        category = category,
                        date = System.currentTimeMillis()
                    )
                    onSave(dreamToSave)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (dream?.id == 0 || dream == null) "Save Dream" else "Update Dream")
        }

        // Delete Dream Button (only when editing an existing dream)
        if (dream != null && dream?.id != 0) {
            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Dream")
            }
        }
    }

    // Confirmation Dialog for Deletion
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Dream") },
            text = { Text("Are you sure you want to delete this dream? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        dream?.let {
                            onDelete(it)
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}