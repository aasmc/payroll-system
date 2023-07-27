package ru.aasmc.payrolsystem.domain.vo

import ru.aasmc.payrolsystem.domain.common.ValueObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import javax.persistence.Embeddable
import javax.persistence.MappedSuperclass

@MappedSuperclass
sealed class PaySchedule : ValueObject {
    abstract fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean

    abstract fun getPayPeriodStartDay(lastPayDay: LocalDate?, day: LocalDate): LocalDate
}

@Embeddable
data class WeeklySchedule(
        private val dayOfPayment: DayOfWeek = DayOfWeek.FRIDAY
) : PaySchedule() {
    override fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean {
        return day.dayOfWeek == dayOfPayment
    }

    override fun getPayPeriodStartDay(lastPayDay: LocalDate?, day: LocalDate): LocalDate {
        return lastPayDay ?: day.minusDays(5)
    }
}

@Embeddable
data class BiWeeklySchedule(
        private val dayOfPayment: DayOfWeek = DayOfWeek.FRIDAY
) : PaySchedule() {
    override fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean {
        if (day.dayOfWeek != dayOfPayment) return false
        return lastPayDay == null || Period.between(lastPayDay, day).days == 14
    }

    override fun getPayPeriodStartDay(lastPayDay: LocalDate?, day: LocalDate): LocalDate {
        return lastPayDay ?: day.minusDays(13)
    }
}

@Embeddable
class MonthlySchedule : PaySchedule() {
    override fun isPayDay(lastPayDay: LocalDate?, day: LocalDate): Boolean {
        val nextMonth = day.plusDays(1).monthValue
        return day.monthValue != nextMonth
    }

    override fun getPayPeriodStartDay(lastPayDay: LocalDate?, day: LocalDate): LocalDate {
        return lastPayDay ?: day.minusDays(day.dayOfMonth.toLong() - 1)
    }
}