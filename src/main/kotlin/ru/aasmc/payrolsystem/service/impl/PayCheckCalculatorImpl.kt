package ru.aasmc.payrolsystem.service.impl

import org.springframework.stereotype.Service
import ru.aasmc.payrolsystem.dto.PayCheck
import ru.aasmc.payrolsystem.dto.PayPeriod
import ru.aasmc.payrolsystem.model.Employee
import ru.aasmc.payrolsystem.repository.ServiceChargeRepository
import ru.aasmc.payrolsystem.service.PayCheckCalculator
import java.math.BigDecimal
import java.time.LocalDate

@Service
class PayCheckCalculatorImpl(
        private val serviceChargeRepository: ServiceChargeRepository
) : PayCheckCalculator {

    override fun calculatePayCheck(employee: Employee, date: LocalDate): PayCheck {
        val paySchedule = employee.paymentSchedule
        val startOfPeriod = paySchedule.getPayPeriodStartDate(employee.lastPayDay, date)
        val payPeriod = PayPeriod(startOfPeriod, date)
        if (paySchedule.isPayDay(employee.lastPayDay, date)) {
            val gross = getGross(employee, payPeriod)
            val deductions = getDeductions(employee, payPeriod)
            val netPay = gross - deductions
            return PayCheck(
                    employeeId = employee.id!!,
                    grossPay = gross,
                    deductions = deductions,
                    netPay = netPay,
                    payPeriod = payPeriod
            )
        } else {
            return PayCheck(
                    employeeId = employee.id!!,
                    grossPay = BigDecimal.ZERO,
                    deductions = BigDecimal.ZERO,
                    netPay = BigDecimal.ZERO,
                    payPeriod = payPeriod
            )
        }

    }

    private fun getGross(employee: Employee, payPeriod: PayPeriod): BigDecimal {
        return employee.paymentType.calculatePaymentFor(payPeriod)
    }

    private fun getDeductions(employee: Employee, payPeriod: PayPeriod): BigDecimal {
        val affIdToServiceCharges = serviceChargeRepository.findAllByEmployee_IdAndDateBetweenAndAffiliation_IdIn(
                employee.id!!,
                payPeriod.start,
                payPeriod.end,
                employee.affiliations.mapNotNull { it.id }.toSet()
        ).groupBy { it.affiliation.id }

        return employee.affiliations.fold(BigDecimal.ZERO) { acc, aff ->
            acc + aff.calculateDeductions(payPeriod, affIdToServiceCharges[aff.id] ?: emptyList())
        }
    }
}