package com.example.sleepezdreamdiary.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DreamSearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    TextField(value = query, onValueChange = {
        query = it
        onSearch(it)
    }, label = { Text("Search Dreams") }, modifier = Modifier.fillMaxWidth()
    )
}