package ru.aasmc.payrolsystem.dto

import java.time.LocalDate

data class TimeCardRequest(
        val date: LocalDate,
        val hours: Int,
        val employeeId: Long
)
