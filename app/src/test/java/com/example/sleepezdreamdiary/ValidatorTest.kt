// ValidatorTest.kt
package com.example.sleepezdreamdiary

import com.example.sleepezdreamdiary.utils.ValidationUtils.validateDream
import org.junit.Assert.*
import org.junit.Test

class ValidatorTest {

    @Test
    fun validateDream_withValidInput_returnsTrue() {
        val title = "A good dream"
        val content = "I had a wonderful dream."
        assertTrue(validateDream(title, content))
    }

    @Test
    fun validateDream_withEmptyTitle_returnsFalse() {
        val title = ""
        val content = "I had a dream."
        assertFalse(validateDream(title, content))
    }

    @Test
    fun validateDream_withEmptyContent_returnsFalse() {
        val title = "Dream Title"
        val content = ""
        assertFalse(validateDream(title, content))
    }

    @Test
    fun validateDream_withEmptyTitleAndContent_returnsFalse() {
        val title = ""
        val content = ""
        assertFalse(validateDream(title, content))
    }
}