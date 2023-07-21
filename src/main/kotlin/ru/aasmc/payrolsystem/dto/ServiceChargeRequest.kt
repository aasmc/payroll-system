package ru.aasmc.payrolsystem.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ServiceChargeRequest(
        val date: LocalDate,
        val amount: BigDecimal,
        val employeeId: Long,
        val affiliationId: Long
)
