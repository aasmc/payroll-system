package ru.aasmc.payrolsystem.util

import ru.aasmc.payrolsystem.dto.PayPeriod
import java.time.LocalDate

fun isInPayPeriod(date: LocalDate, period: PayPeriod): Boolean {
    return date.isAfter(period.start.minusDays(1)) && date.isBefore(period.end.plusDays(1))
}

fun isInPayPeriod(date: LocalDate, period: ru.aasmc.payrolsystem.domain.vo.PayPeriod): Boolean {
    return date.isAfter(period.start.minusDays(1)) && date.isBefore(period.end.plusDays(1))
}