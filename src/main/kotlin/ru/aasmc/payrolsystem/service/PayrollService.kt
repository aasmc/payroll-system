package ru.aasmc.payrolsystem.service

import ru.aasmc.payrolsystem.dto.PayCheckResponse
import java.time.LocalDate

interface PayrollService {
    fun makePay(date: LocalDate): PayCheckResponse
}