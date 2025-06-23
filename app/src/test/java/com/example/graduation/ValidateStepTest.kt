package com.example.graduation
import org.junit.Assert.*
import org.junit.Test
import com.example.graduation.utils.validateStep

class ValidateStepTest {

    private val MAX_ROWS = 1_000_000.00
    private val TOTAL_MM = 1_000.0

    @Test fun stepMustBePositive() {
        assertEquals("Шаг должен быть > 0", validateStep(TOTAL_MM, 0.0))
        assertEquals("Шаг должен быть > 0", validateStep(TOTAL_MM, -5.0))
    }

    @Test fun tooManyRowsWarnsUser() {
        val tinyStep = TOTAL_MM / (MAX_ROWS + 1)
        val msg = validateStep(TOTAL_MM, tinyStep)

        assertNotNull(msg)
        assertTrue(msg!!.startsWith("Таблица будет слишком большой"))
    }

    @Test fun exactlyMaxRowsIsAllowed() {
        val borderStep = TOTAL_MM / MAX_ROWS
        assertNull(validateStep(TOTAL_MM, borderStep))
    }

    @Test fun goodStepReturnsNull() {
        val niceStep = TOTAL_MM / 100
        assertNull(validateStep(TOTAL_MM, niceStep))
    }
}