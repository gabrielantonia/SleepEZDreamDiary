package com.example.sleepezdreamdiary.data.repository

import com.example.sleepezdreamdiary.data.dao.DreamDao
import com.example.sleepezdreamdiary.data.model.Dream
import kotlinx.coroutines.flow.Flow

class DreamRepository(private val dreamDao: DreamDao) {
    fun getDreamById(dreamId: Long): Flow<Dream?> = dreamDao.getDreamById(dreamId)
    fun getRecentDreams(): Flow<List<Dream>> = dreamDao.getRecentDreams()
    fun getDreamsByCategory(category: String): Flow<List<Dream>> =
        dreamDao.getDreamsByCategory(category)

    fun searchDreams(query: String): Flow<List<Dream>> = dreamDao.searchDreams(query)
    suspend fun insertDream(dream: Dream) = dreamDao.insertDream(dream)
    suspend fun updateDream(dream: Dream) = dreamDao.updateDream(dream)
    suspend fun deleteDream(dream: Dream) = dreamDao.deleteDream(dream)

    fun getDreamsByDateRange(startDate: Long, endDate: Long): Flow<List<Dream>> {
        return dreamDao.getDreamsByDateRange(startDate, endDate)
    }
}