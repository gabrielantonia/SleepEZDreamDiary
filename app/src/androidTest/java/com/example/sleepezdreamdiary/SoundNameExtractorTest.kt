package com.example.sleepezdreamdiary

import android.database.Cursor
import android.database.MatrixCursor
import android.provider.OpenableColumns
import org.junit.Assert.*
import org.junit.Test

class SoundNameExtractorTest {

    @org.junit.Test
    fun extractSoundNameFromCursor_withValidCursor_returnsSoundName() {
        val cursor =
            android.database.MatrixCursor(kotlin.arrayOf(android.provider.OpenableColumns.DISPLAY_NAME))
        cursor.addRow(kotlin.arrayOf("Test Sound"))

        val soundName = extractSoundNameFromCursor(cursor)
        assertEquals("Test Sound", soundName)
    }

    @Test
    fun extractSoundNameFromCursor_withNullCursor_returnsUnknownSound() {
        val soundName = extractSoundNameFromCursor(null)
        assertEquals("Unknown Sound", soundName)
    }

    @Test
    fun extractSoundNameFromCursor_withEmptyCursor_returnsUnknownSound() {
        val cursor = MatrixCursor(arrayOf(OpenableColumns.DISPLAY_NAME))
        val soundName = extractSoundNameFromCursor(cursor)
        assertEquals("Unknown Sound", soundName)
    }
}

fun extractSoundNameFromCursor(cursor: Cursor?): String {
    var soundName = "Unknown Sound"
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                soundName = it.getString(nameIndex)
            }
        }
    }
    return soundName
}