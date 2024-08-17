package com.example.familyconnectv2.decorators

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class WeeklyFacade {

    fun getDayOfWeekLabels(): Array<String> {
        return arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    }

    fun getFormattedMonthYear(startOfWeek: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
        return startOfWeek.format(formatter)
    }

    fun getDaysInWeek(startOfWeek: LocalDate): List<LocalDate> {
        val endOfWeek = startOfWeek.plusDays(6)
        val daysInWeek = arrayListOf<LocalDate>()
        var currentDate = startOfWeek
        while (currentDate <= endOfWeek) {
            daysInWeek.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
        return daysInWeek
    }

    fun getPreviousWeekStartDate(currentWeekStartDate: LocalDate): LocalDate {
        return currentWeekStartDate.minusWeeks(1)
    }

    fun getNextWeekStartDate(currentWeekStartDate: LocalDate): LocalDate {
        return currentWeekStartDate.plusWeeks(1)
    }
}
