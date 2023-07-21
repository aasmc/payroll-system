package ru.aasmc.payrolsystem.dto

import java.math.BigDecimal
import java.time.LocalDate

data class PayCheckResponse(
        val paychecks: List<PayCheck>
)

data class PayCheck(
        val employeeId: Long,
        val grossPay: BigDecimal,
        val deductions: BigDecimal,
        val netPay: BigDecimal,
        val payPeriod: PayPeriod
)


data class PayPeriod(
        val start: LocalDate,
        val end: LocalDate
)