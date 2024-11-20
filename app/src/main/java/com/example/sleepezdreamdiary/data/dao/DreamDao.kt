package com.example.sleepezdreamdiary.data.dao

import androidx.room.*
import com.example.sleepezdreamdiary.data.model.Dream
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Query("SELECT * FROM dreams ORDER BY date DESC LIMIT 5")
    fun getRecentDreams(): Flow<List<Dream>>

    @Query("SELECT * FROM dreams WHERE category = :category")
    fun getDreamsByCategory(category: String): Flow<List<Dream>>

    @Query("SELECT * FROM dreams WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchDreams(query: String): Flow<List<Dream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDream(dream: Dream)

    @Update
    suspend fun updateDream(dream: Dream)

    @Delete
    suspend fun deleteDream(dream: Dream)

    @Query("SELECT * FROM dreams WHERE id = :dreamId LIMIT 1")
    fun getDreamById(dreamId: Long): Flow<Dream?>

    @Query("SELECT * FROM dreams WHERE date BETWEEN :startDate AND :endDate")
    fun getDreamsByDateRange(startDate: Long, endDate: Long): Flow<List<Dream>>
}