package com.example.sleepezdreamdiary.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import com.example.sleepezdreamdiary.data.model.Dream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Date
import kotlin.collections.forEach


suspend fun generateDreamReportUtil(
    context: Context, dreams: List<Dream>, startDate: Long, endDate: Long, folderUri: Uri
) {
    val reportBuilder = StringBuilder()

    // Title and timestamp
    val currentDateTime = DateFormat.getDateTimeInstance().format(Date())
    reportBuilder.append("Dream Report\n")
    reportBuilder.append("Generated on: $currentDateTime\n")
    reportBuilder.append(
        "Report Period: ${
            DateFormat.getDateInstance().format(Date(startDate))
        } - ${DateFormat.getDateInstance().format(Date(endDate))}\n"
    )
    reportBuilder.append("=".repeat(50)).append("\n\n")

    // Headers for the table
    reportBuilder.append(
        String.format(
            "%-20s %-10s %-15s %-50s\n", "Title", "Category", "Date", "Content"
        )
    )
    reportBuilder.append("-".repeat(95)).append("\n")

    // Rows of dreams
    dreams.forEach { dream ->
        val dreamDate = DateFormat.getDateInstance().format(Date(dream.date))
        val truncatedContent =
            if (dream.content.length > 50) dream.content.take(47) + "..." else dream.content

        reportBuilder.append(
            String.format(
                "%-20s %-10s %-15s %-50s\n",
                dream.title,
                dream.category,
                dreamDate,
                truncatedContent
            )
        )
    }

    // Prepare to write the report to a file
    withContext(Dispatchers.IO) {
        try {
            val folderDocumentFile = DocumentFile.fromTreeUri(context, folderUri)

            if (folderDocumentFile != null && folderDocumentFile.isDirectory) {
                val fileName = "DreamReport_${System.currentTimeMillis()}.txt"
                val reportFile = folderDocumentFile.createFile("text/plain", fileName)

                if (reportFile != null) {
                    context.contentResolver.openOutputStream(reportFile.uri)?.use { outputStream ->
                        outputStream.write(reportBuilder.toString().toByteArray())
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context, "Report saved to ${reportFile.uri.path}", Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context, "Failed to create report file.", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Invalid folder location.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, "Failed to save report: ${e.message}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}