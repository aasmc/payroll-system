package ru.aasmc.payrolsystem.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period

enum class PaymentSchedule {
    WEEKLY {
        override fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean {
            return day.dayOfWeek == DayOfWeek.FRIDAY
        }

        override fun getPayPeriodStartDate(lastPayDay: LocalDate?, date: LocalDate): LocalDate {
            return lastPayDay ?: date.minusDays(5)
        }

    },
    BIWEEKLY {
        override fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean {
            if (day.dayOfWeek != DayOfWeek.FRIDAY) return false
            return lastPayDay == null || Period.between(lastPayDay, day).days == 14
        }

        override fun getPayPeriodStartDate(lastPayDay: LocalDate?, date: LocalDate): LocalDate {
            return lastPayDay ?: date.minusDays(13)
        }
    },
    MONTHLY {
        override fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean {
            val nextMonth = day.plusDays(1).monthValue
            return day.monthValue != nextMonth
        }

        override fun getPayPeriodStartDate(lastPayDay: LocalDate?, date: LocalDate): LocalDate {
            return lastPayDay ?: date.minusDays(date.dayOfMonth.toLong() - 1)
        }
    };

    abstract fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean

    abstract fun getPayPeriodStartDate(lastPayDay: LocalDate?, date: LocalDate): LocalDate
}

