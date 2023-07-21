package ru.aasmc.payrolsystem.service

import ru.aasmc.payrolsystem.dto.PayCheck
import ru.aasmc.payrolsystem.model.Employee
import java.time.LocalDate

interface PayCheckCalculator {

    fun calculatePayCheck(employee: Employee, date: LocalDate): PayCheck

}