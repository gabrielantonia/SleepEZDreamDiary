package com.example.sleepezdreamdiary.presentation.ui.log

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.example.sleepezdreamdiary.presentation.ui.components.DreamCarousel
import com.example.sleepezdreamdiary.presentation.ui.components.DreamReportDialog
import com.example.sleepezdreamdiary.presentation.ui.components.DreamSearchBar
import com.example.sleepezdreamdiary.presentation.viewmodel.LogViewModel

@Composable
fun LogScreen(navController: NavController, logViewModel: LogViewModel, context: Context) {
    val searchResults by logViewModel.searchResults.collectAsState()
    var showSearchResults by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar (Always Visible at the Top)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                DreamSearchBar(onSearch = {
                    logViewModel.searchDreams(it)
                    showSearchResults = it.isNotBlank()
                })
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (showSearchResults && searchResults.isNotEmpty()) {
                    item {
                        DreamCarousel(dreams = searchResults, onClick = { dream ->
                            navController.navigate("add_dream?dreamId=${dream.id}")
                        })
                    }
                } else {
                    val categories = listOf("Recent", "Vivid", "Nightmare", "Lucid", "Other")
                    categories.forEach { category ->
                        item {
                            val dreams by if (category == "Recent") logViewModel.recentDreams.collectAsState(
                                initial = emptyList()
                            ) else logViewModel.getDreamsByCategory(category)
                                .collectAsState(initial = emptyList())
                            Text(
                                "$category Dreams", style = MaterialTheme.typography.headlineSmall
                            )

                            DreamCarousel(dreams = dreams, onClick = { dream ->
                                navController.navigate("add_dream?dreamId=${dream.id}")
                            })
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        // Floating Action Button to add a new dream
        FloatingActionButton(
            onClick = {
                navController.navigate("add_dream")
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Dream")
        }

        // Secondary FAB to generate a dream report
        FloatingActionButton(
            onClick = {
                showReportDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = (-72).dp) // Offset to place below the add dream FAB
        ) {
            Icon(Icons.Default.DateRange, contentDescription = "Generate Dream Report")
        }
        LocalContext.current
        if (showReportDialog) {
            DreamReportDialog(onDismiss = { showReportDialog = false },
                onGenerateReport = { startDate, endDate, folderUri ->
                    logViewModel.generateDreamReport(startDate, endDate, folderUri, context)
                    showReportDialog = false
                })
        }
    }
}