package pl.pregiel.workwork.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object Utils {
    @JvmStatic
    fun formatDoubleToString(value: Double) = "%.2f".format(value)

    @JvmStatic
    fun formatTimeAmount(minutes: Int) =
            "%1\$s h".format(formatDoubleToString((minutes.toDouble() / 60.0)))

    @JvmStatic
    fun timeMinutesToString(minutes: Int) =
            "%1\$02d:%2\$02d".format(abs(minutes / 60), abs(minutes % 60))

    @JvmStatic
    @Throws(NumberFormatException::class, ParseException::class)
    fun timeStringToMinutes(time: String): Int {
        if (!time.contains(":")) throw ParseException(time, -1)

        val timeSplit = time.split(":")
        val hour = timeSplit[0].toInt() * 60
        val minute = timeSplit[1].toInt()

        return hour + minute
    }

    @JvmStatic
    fun timeMinutesToStringWithSuffixes(minutes: Int) =
            "%1\$dh %2\$dm".format(abs(minutes / 60), abs(minutes % 60))

    @JvmStatic
    @Throws(NumberFormatException::class, ParseException::class)
    fun timeStringWithSuffixesToMinutes(time: String): Int {
        if (!time.contains("h *".toRegex())) throw ParseException(time, -1)

        val timeSplit = time.split("h *".toRegex())
        val hour = timeSplit[0].replace("\\D".toRegex(), "").toInt() * 60
        val minute = timeSplit[1].replace("\\D".toRegex(), "").toInt()

        return hour + minute
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun stringToCalendar(day: String, regex: String): Calendar {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(regex, Locale.ENGLISH)
        calendar.time = dateFormat.parse(day)
        return calendar
    }

}