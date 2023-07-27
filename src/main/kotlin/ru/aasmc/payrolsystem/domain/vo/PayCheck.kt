package ru.aasmc.payrolsystem.domain.vo

import ru.aasmc.payrolsystem.domain.common.ValueObject
import java.math.BigDecimal
import java.time.LocalDate

data class PayCheck(
        val employeeId: String,
        val grossPay: BigDecimal,
        val deductions: BigDecimal,
        val netPay: BigDecimal,
        val payPeriod: PayPeriod
): ValueObject


data class PayPeriod(
        val start: LocalDate,
        val end: LocalDate
)
