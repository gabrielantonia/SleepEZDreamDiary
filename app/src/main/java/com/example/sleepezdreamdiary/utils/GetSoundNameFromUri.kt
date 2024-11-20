package com.example.sleepezdreamdiary.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

fun getSoundNameFromUri(context: Context, uri: Uri): String {
    var soundName = "Unknown Sound"

    val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
    var cursor: Cursor? = null

    try {
        cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                soundName = cursor.getString(nameIndex)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor?.close()
    }

    return soundName
}