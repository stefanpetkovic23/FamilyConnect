package com.example.familyconnectv2.decorators

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MonthlyFacade {

    private var currentDate: LocalDate = LocalDate.now()

    fun updateCalendar() {
        val monthYearFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
        val monthYearString = currentDate.format(monthYearFormatter)

    }

     fun goToPreviousMonth() {
        currentDate = currentDate.minusMonths(1)
        updateCalendar()
    }

     fun goToNextMonth() {
        currentDate = currentDate.plusMonths(1)
        updateCalendar()
    }

     fun generateCalendarDates(): List<String> {
        val dates = mutableListOf<String>()
        val daysInMonth = currentDate.lengthOfMonth()
        val firstDayOfMonth = LocalDate.of(currentDate.year, currentDate.month, 1)
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value

        // Dodavanje praznih mesta za dane pre prvog dana meseca
        for (i in 1 until startDayOfWeek) {
            dates.add("")
        }

        // Dodavanje dana u mesec
        for (dayOfMonth in 1..daysInMonth) {
            dates.add(dayOfMonth.toString())
        }
        return dates
    }


}