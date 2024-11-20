package com.example.sleepezdreamdiary.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sleepezdreamdiary.data.repository.AlarmRepository
import com.example.sleepezdreamdiary.data.repository.DreamRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    application: Application,
    private val dreamRepository: DreamRepository,
    private val alarmRepository: AlarmRepository
) : AndroidViewModel(application) {

    // ViewModel for alarms
    val alarmViewModel: AlarmViewModel = AlarmViewModel(application, alarmRepository)

    val recentDreams: StateFlow<List<com.example.sleepezdreamdiary.data.model.Dream>> =
        dreamRepository.getRecentDreams()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

class HomeViewModelFactory(
    private val application: Application,
    private val dreamRepository: DreamRepository,
    private val alarmRepository: AlarmRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, dreamRepository, alarmRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}