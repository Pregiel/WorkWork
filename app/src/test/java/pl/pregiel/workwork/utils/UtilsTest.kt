package pl.pregiel.workwork.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.text.ParseException
import java.util.*
import kotlin.test.assertFailsWith

internal class UtilsTest {

    @Test
    fun formatDoubleToString_validInput_returnString() {
        val input = 128.1234
        val result = Utils.formatDoubleToString(input)

        assertEquals("128,12", result)
    }

    @Test
    fun formatDoubleToString_validInput_returnStringRoundedUp() {
        val input = 128.1259

        val result = Utils.formatDoubleToString(input)

        assertEquals("128,13", result)
    }

    @Test
    fun formatTimeAmount_validInput_returnFormattedTimeString() {
        val input = 150

        val result = Utils.formatTimeAmount(input)

        assertEquals("2,50 h", result)
    }

    @Test
    fun timeMinutesToString_validInput_returnFormattedTimeString() {
        val input = 156

        val result = Utils.timeMinutesToString(input)

        assertEquals("02:36", result)
    }

    @Test
    fun timeStringToMinutes_validInput_returnTimeInMinutes() {
        val input = "02:36"

        val result = Utils.timeStringToMinutes(input)

        assertEquals(156, result)
    }

    @Test
    fun timeStringToMinutes_invalidInput_throwNumberFormatException() {
        val input = "02;36"

        assertFailsWith<ParseException> { Utils.timeStringToMinutes(input) }
    }

    @Test
    fun timeMinutesToStringWithSuffixes_validInput_returnTimeString() {
        val input = 156

        val result = Utils.timeMinutesToStringWithSuffixes(input)

        assertEquals("2h 36m", result)
    }

    @Test
    fun timeStringWithSuffixesToMinutes_validInput_returnTimeInMinutes() {
        val input = "2h 36m"

        val result = Utils.timeStringWithSuffixesToMinutes(input)

        assertEquals(156, result)
    }

    @Test
    fun timeStringWithSuffixesToMinutes_invalidInput_throwNumberFormatException() {
        val input = "2g 36m"

        assertFailsWith<ParseException> { Utils.timeStringWithSuffixesToMinutes(input) }
    }

    @Test
    fun stringToCalendar_validInputs_returnCalendar() {
        val inputDate = "05.06.2019"
        val inputRegex = "dd.MM.yyyy"

        val result = Utils.stringToCalendar(inputDate, inputRegex)

        assertAll(
                { assertEquals(result.get(Calendar.DAY_OF_MONTH), 5) },
                { assertEquals(result.get(Calendar.MONTH), 5) },
                { assertEquals(result.get(Calendar.YEAR), 2019) }
        )
    }

    @Test
    fun stringToCalendar_invalidDate_returnCalendar() {
        val inputDate = "05506.2019"
        val inputRegex = "dd.MM.yyyy"

        assertFailsWith<ParseException> { Utils.stringToCalendar(inputDate, inputRegex) }
    }

}