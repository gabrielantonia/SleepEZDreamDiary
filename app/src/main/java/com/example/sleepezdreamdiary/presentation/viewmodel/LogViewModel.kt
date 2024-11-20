package com.example.sleepezdreamdiary.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sleepezdreamdiary.data.model.Dream
import com.example.sleepezdreamdiary.data.repository.DreamRepository
import com.example.sleepezdreamdiary.utils.generateDreamReportUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogViewModel(
    application: Application,
    private val dreamRepository: DreamRepository
) : AndroidViewModel(application) {

    val searchResults = MutableStateFlow<List<Dream>>(emptyList())

    fun searchDreams(query: String) {
        viewModelScope.launch {
            dreamRepository.searchDreams(query).collect {
                searchResults.value = it
            }
        }
    }

    fun generateDreamReport(startDate: Long, endDate: Long, folderUri: Uri, context: Context) {
        viewModelScope.launch {
            val dreams =
                getDreamsByDateRange(startDate, endDate).first() // Collect the flow to get the list
            generateDreamReportUtil(
                context = context,
                dreams = dreams,
                startDate = startDate,
                endDate = endDate,
                folderUri = folderUri
            )
        }
    }

    fun getDreamsByDateRange(startDate: Long, endDate: Long): Flow<List<Dream>> {
        return dreamRepository.getDreamsByDateRange(startDate, endDate)
    }

    val recentDreams: StateFlow<List<Dream>> = dreamRepository.getRecentDreams()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getDreamsByCategory(category: String): Flow<List<Dream>> =
        dreamRepository.getDreamsByCategory(category)

    fun addDream(dream: Dream) {
        viewModelScope.launch {
            dreamRepository.insertDream(dream)
        }
    }

    fun deleteDream(dream: Dream) {
        viewModelScope.launch {
            dreamRepository.deleteDream(dream)
        }
    }

    // New function to update a dream
    fun updateDream(dream: Dream) {
        viewModelScope.launch {
            dreamRepository.updateDream(dream)
        }
    }

    // Function to get a dream by ID
    fun getDreamById(dreamId: Long): Flow<Dream?> = dreamRepository.getDreamById(dreamId)
}

class LogViewModelFactory(
    private val application: Application,
    private val dreamRepository: DreamRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogViewModel(application, dreamRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
