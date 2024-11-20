package com.example.sleepezdreamdiary.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleepezdreamdiary.presentation.ui.components.DreamCard
import com.example.sleepezdreamdiary.presentation.ui.components.SimpleAlarmItem
import com.example.sleepezdreamdiary.presentation.viewmodel.HomeViewModel


@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val recentDreams by homeViewModel.recentDreams.collectAsState()
    val upcomingAlarms by homeViewModel.alarmViewModel.activeAlarms.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Recent Dreams", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        if (recentDreams.isEmpty()) {
            Text(
                text = "You have no recent dreams recorded. Tap here to add one.",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate("add_dream")
                    }
            )
        } else {
            // Recent Dreams Carousel
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(recentDreams) { dream ->
                    DreamCard(dream = dream, onClick = { selectedDream ->
                        navController.navigate("add_dream?dreamId=${selectedDream.id}")
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Upcoming Alarms", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        if (upcomingAlarms.isEmpty()) {
            Text(
                "You have no active alarms.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            // Upcoming Alarms List
            upcomingAlarms.forEach { alarm ->
                SimpleAlarmItem(alarm = alarm, alarmViewModel = homeViewModel.alarmViewModel)
            }
        }
    }
}