package com.example.sleepezdreamdiary.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Dream

@Composable
fun DreamCarousel(dreams: List<Dream>, onClick: (Dream) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(dreams) { dream ->
            DreamCard(dream = dream, onClick = onClick)
        }
    }
}