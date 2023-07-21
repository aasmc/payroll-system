package ru.aasmc.payrolsystem.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.aasmc.payrolsystem.dto.PayCheck
import ru.aasmc.payrolsystem.dto.PayCheckResponse
import ru.aasmc.payrolsystem.model.Employee
import ru.aasmc.payrolsystem.repository.EmployeeRepository
import ru.aasmc.payrolsystem.service.PayCheckCalculator
import ru.aasmc.payrolsystem.service.PayrollService
import java.math.BigDecimal
import java.time.LocalDate

@Service
class PayrollServiceImpl(
        private val employeeRepository: EmployeeRepository,
        private val payCheckCalculator: PayCheckCalculator
) : PayrollService {


    @Transactional
    override fun makePay(date: LocalDate): PayCheckResponse {
        val employees = employeeRepository.findAll()
        val payChecks = employees.map { employee ->
            val check = payCheckCalculator.calculatePayCheck(employee, date)
            updateLastPayDayIfHappened(employee, date, check)
            check
        }
        return PayCheckResponse(payChecks)
    }

    private fun updateLastPayDayIfHappened(employee: Employee, date: LocalDate, check: PayCheck) {
        if (check.grossPay != BigDecimal.ZERO) {
            employee.lastPayDay = date
            employeeRepository.save(employee)
        }
    }
}