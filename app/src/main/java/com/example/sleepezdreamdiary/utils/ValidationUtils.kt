package com.example.sleepezdreamdiary.utils


object ValidationUtils {
    fun validateDream(title: String, content: String): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }

    fun validateAlarm(time: Long): Boolean {
        return time > System.currentTimeMillis()
    }
}