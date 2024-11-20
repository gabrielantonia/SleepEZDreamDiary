package com.example.sleepezdreamdiary

import com.example.sleepezdreamdiary.data.model.Dream
import org.junit.Assert.*
import org.junit.Test

class DreamFilterTest {

    @Test
    fun filterDreamsByCategory_withMatchingCategory_returnsFilteredDreams() {
        val dreams = listOf(
            Dream(id = 1, title = "Dream 1", content = "Content 1", category = "Lucid", date = 0L),
            Dream(id = 2, title = "Dream 2", content = "Content 2", category = "Vivid", date = 0L),
            Dream(id = 3, title = "Dream 3", content = "Content 3", category = "Lucid", date = 0L)
        )

        val dreamFilter = DreamFilter()
        val filteredDreams = dreamFilter.filterDreamsByCategory(dreams, "Lucid")

        assertEquals(2, filteredDreams.size)
        assertTrue(filteredDreams.all { it.category == "Lucid" })
    }

    @Test
    fun filterDreamsByCategory_withNoMatchingCategory_returnsEmptyList() {
        val dreams = listOf(
            Dream(id = 1, title = "Dream 1", content = "Content 1", category = "Vivid", date = 0L),
            Dream(
                id = 2, title = "Dream 2", content = "Content 2", category = "Nightmare", date = 0L
            )
        )

        val dreamFilter = DreamFilter()
        val filteredDreams = dreamFilter.filterDreamsByCategory(dreams, "Lucid")

        assertTrue(filteredDreams.isEmpty())
    }
}


class DreamFilter {
    fun filterDreamsByCategory(dreams: List<Dream>, category: String): List<Dream> {
        return dreams.filter { it.category == category }
    }
}