package com.example.sleepezdreamdiary.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sleepezdreamdiary.data.model.Dream
import java.text.DateFormat
import java.util.Date

@Composable
fun DreamCard(dream: Dream, onClick: (Dream) -> Unit) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(8.dp)
            .clickable { onClick(dream) },
        elevation = cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = dream.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = dream.category, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = dream.content, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = DateFormat.getDateInstance().format(Date(dream.date)),
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}