package com.example.sleepezdreamdiary.presentation.ui

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sleepezdreamdiary.data.database.SleepEZDatabase
import com.example.sleepezdreamdiary.data.repository.AlarmRepository
import com.example.sleepezdreamdiary.data.repository.DreamRepository
import com.example.sleepezdreamdiary.presentation.ui.add_alarm.AddAlarmScreen
import com.example.sleepezdreamdiary.presentation.ui.add_dream.AddDreamScreen
import com.example.sleepezdreamdiary.presentation.ui.alarms.AlarmScreen
import com.example.sleepezdreamdiary.presentation.ui.authentication.AuthenticationScreen
import com.example.sleepezdreamdiary.presentation.ui.components.BottomNavBar
import com.example.sleepezdreamdiary.presentation.ui.home.HomeScreen
import com.example.sleepezdreamdiary.presentation.ui.log.LogScreen
import com.example.sleepezdreamdiary.presentation.ui.splash.SplashScreen
import com.example.sleepezdreamdiary.presentation.viewmodel.AlarmViewModel
import com.example.sleepezdreamdiary.presentation.viewmodel.AlarmViewModelFactory
import com.example.sleepezdreamdiary.presentation.viewmodel.HomeViewModel
import com.example.sleepezdreamdiary.presentation.viewmodel.HomeViewModelFactory
import com.example.sleepezdreamdiary.presentation.viewmodel.LogViewModel
import com.example.sleepezdreamdiary.presentation.viewmodel.LogViewModelFactory
import kotlin.collections.contains

@Composable
fun SleepEZDreamDiaryApp() {
    val navController = rememberNavController()

    // Getting the current back stack entry to determine the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Access the application context
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Create database instance and repositories
    val database = SleepEZDatabase.getDatabase(application)
    val dreamRepository = DreamRepository(database.dreamDao())
    val alarmRepository = AlarmRepository(database.alarmDao())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Conditionally display the BottomNavBar if the user is not on the authentication or splash screen
            if (currentRoute !in listOf("authentication", "splash")) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash", // Set splash as the start destination
            Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("authentication") {
                AuthenticationScreen(navController)
            }
            composable("home") {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(
                        application, dreamRepository, alarmRepository
                    )
                )
                HomeScreen(navController, homeViewModel)
            }
            composable("log") {
                val logViewModel: LogViewModel = viewModel(
                    factory = LogViewModelFactory(application, dreamRepository)
                )
                LogScreen(navController, logViewModel, context)
            }
            composable("alarms") {
                val alarmViewModel: AlarmViewModel = viewModel(
                    factory = AlarmViewModelFactory(application, alarmRepository)
                )
                AlarmScreen(alarmViewModel)
            }
            composable(
                "add_dream?dreamId={dreamId}", arguments = listOf(navArgument("dreamId") {
                    type = NavType.LongType
                    defaultValue = -1L // Use -1 to represent that no dream is being edited
                })
            ) { backStackEntry ->
                val dreamId = backStackEntry.arguments?.getLong("dreamId") ?: -1L
                val logViewModel: LogViewModel = viewModel(
                    factory = LogViewModelFactory(application, dreamRepository)
                )

                AddDreamScreen(
                    existingDreamId = dreamId,
                    logViewModel = logViewModel,
                    onSave = { dream ->
                        if (dream.id == 0) {
                            logViewModel.addDream(dream)
                        } else {
                            logViewModel.updateDream(dream)
                        }
                        navController.popBackStack() // Navigate back after saving
                    },
                    onDelete = { dream ->
                        logViewModel.deleteDream(dream)
                        navController.popBackStack() // Navigate back after deleting
                    }
                )
            }

            composable("add_alarm") {
                val alarmViewModel: AlarmViewModel = viewModel(
                    factory = AlarmViewModelFactory(application, alarmRepository)
                )
                AddAlarmScreen(onSave = { alarm ->
                    alarmViewModel.addAlarm(alarm)
                    navController.popBackStack() // Navigate back after saving
                })
            }
        }
    }
}