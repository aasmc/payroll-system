package ru.aasmc.payrolsystem.dto

import java.math.BigDecimal
import java.time.LocalDate

data class SalesReceiptRequest(
        val employeeId: Long,
        val date: LocalDate,
        val amount: BigDecimal
)
