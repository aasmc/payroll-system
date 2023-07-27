package ru.aasmc.payrolsystem.domain.service

import ru.aasmc.payrolsystem.domain.entity.Employee
import ru.aasmc.payrolsystem.domain.vo.PayCheck
import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.PayType
import java.math.BigDecimal
import java.time.LocalDate

interface PayRollService<T: PayType> {

    fun makePayments(date: LocalDate) {
        findAllEmployees().forEach { employee ->
            val paycheck = createPayCheckFor(employee, date)
            sendPayCheck(paycheck, employee)
            employee.updateLastPayDay(date)
        }
    }

    fun findAllEmployees(): List<Employee<T, *>>

    fun createPayCheckFor(employee: Employee<T, *>, date: LocalDate): PayCheck {
        val scheduler = employee.paySchedule
        val payPeriodStartDay = scheduler.getPayPeriodStartDay(employee.lastPayDay, date)
        val payPeriod = PayPeriod(payPeriodStartDay, date)
        if (scheduler.isPayDay(employee.lastPayDay, date)) {
            val gross = calculateGross(employee, payPeriod)
            val deductions = calculateDeductions(employee, payPeriod)
            val net = calculateNet(gross, deductions)
            return PayCheck(
                    employeeId = employee.getId(),
                    grossPay = gross,
                    deductions = deductions,
                    netPay = net,
                    payPeriod = payPeriod
            )
        }

        return PayCheck(
                employeeId = employee.getId(),
                grossPay = BigDecimal.ZERO,
                deductions = BigDecimal.ZERO,
                netPay = BigDecimal.ZERO,
                payPeriod = payPeriod
        )
    }

    fun sendPayCheck(payCheck: PayCheck, employee: Employee<*, *>)

    fun calculateDeductions(employee: Employee<T, *>, payPeriod: PayPeriod): BigDecimal

    fun calculateGross(employee: Employee<T, *>, payPeriod: PayPeriod): BigDecimal

    fun calculateNet(gross: BigDecimal, deductions: BigDecimal): BigDecimal

}