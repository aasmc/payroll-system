package ru.aasmc.payrolsystem.domain.service.impl

import ru.aasmc.payrolsystem.domain.entity.Employee
import ru.aasmc.payrolsystem.domain.interfaces.EmployeeRepository
import ru.aasmc.payrolsystem.domain.interfaces.PayCheckSender
import ru.aasmc.payrolsystem.domain.service.AffiliationService
import ru.aasmc.payrolsystem.domain.service.PayRollService
import ru.aasmc.payrolsystem.domain.vo.PayCheck
import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.PayType
import java.math.BigDecimal
import kotlin.reflect.KClass

abstract class BasePayRollService<T: PayType>(
        protected val employeeRepository: EmployeeRepository,
        private val payMethodToSender: Map<KClass<*>, PayCheckSender>,
        protected val affiliationService: AffiliationService
) : PayRollService<T> {

    override fun sendPayCheck(payCheck: PayCheck, employee: Employee<*, *>) {
        val sender = payMethodToSender[employee.payMethod::class]
                ?: throw IllegalStateException("No configuration for PayCheck Sender for method ${employee.payMethod::class}.")
        sender.sendPayCheck(payCheck)
    }

    override fun calculateDeductions(employee: Employee<T, *>, payPeriod: PayPeriod): BigDecimal {
        val affiliations = affiliationService.findAllAffiliationsOfEmployee(employee.getId())
        val dues = affiliations.fold(BigDecimal.ZERO) { acc, aff ->
            acc + aff.calculateDeductions(payPeriod)
        }
        val charges = affiliationService.findAllUnpaidServiceChargesOfEmployee(employee.getId())
                .fold(BigDecimal.ZERO) { acc, sc ->
            acc + sc.amount
        }
        return dues + charges
    }


    override fun calculateNet(gross: BigDecimal, deductions: BigDecimal): BigDecimal {
        return gross - deductions
    }

}