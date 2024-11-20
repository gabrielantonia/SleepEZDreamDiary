package com.example.sleepezdreamdiary

import com.example.sleepezdreamdiary.data.model.Dream
import org.junit.Assert.*
import org.junit.Test

class DreamReportGeneratorTest {

    @Test
    fun generateReport_withDreams_returnsFormattedString() {
        val dreams = listOf(
            Dream(id = 1, title = "Dream 1", content = "Content 1", category = "Lucid", date = 0L),
            Dream(id = 2, title = "Dream 2", content = "Content 2", category = "Vivid", date = 0L)
        )

        val report = DreamReportGenerator.generateReport(dreams)
        val expectedReport = """
        Title: Dream 1
        Category: Lucid
        Content: Content 1

        Title: Dream 2
        Category: Vivid
        Content: Content 2
        """.trimIndent()

        assertEquals(expectedReport, report)
    }

    @Test
    fun generateReport_withNoDreams_returnsEmptyString() {
        val dreams = emptyList<Dream>()
        val report = DreamReportGenerator.generateReport(dreams)
        assertTrue(report.isEmpty())
    }
}

object DreamReportGenerator {
    fun generateReport(dreams: List<Dream>): String {
        val reportBuilder = StringBuilder()
        // Simplified report generation logic for testing
        dreams.forEach { dream ->
            reportBuilder.append("Title: ${dream.title}\n")
            reportBuilder.append("Category: ${dream.category}\n")
            reportBuilder.append("Content: ${dream.content}\n\n")
        }
        return reportBuilder.toString().trimEnd()
    }
}
