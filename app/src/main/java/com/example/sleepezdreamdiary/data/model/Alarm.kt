package com.example.sleepezdreamdiary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Long,
    val isActive: Boolean,
    val soundUri: String? = null
)