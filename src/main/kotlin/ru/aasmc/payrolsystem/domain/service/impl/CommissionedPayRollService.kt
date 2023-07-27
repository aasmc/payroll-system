package ru.aasmc.payrolsystem.domain.service.impl

import ru.aasmc.payrolsystem.domain.entity.Employee
import ru.aasmc.payrolsystem.domain.interfaces.EmployeeRepository
import ru.aasmc.payrolsystem.domain.interfaces.PayCheckSender
import ru.aasmc.payrolsystem.domain.interfaces.SalesReceiptRepository
import ru.aasmc.payrolsystem.domain.service.AffiliationService
import ru.aasmc.payrolsystem.domain.vo.CommissionedType
import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import java.math.BigDecimal
import kotlin.reflect.KClass

class CommissionedPayRollService(
        employeeRepository: EmployeeRepository,
        payMethodToSender: Map<KClass<*>, PayCheckSender>,
        affiliationService: AffiliationService,
        private val salesReceiptRepository: SalesReceiptRepository
) : BasePayRollService<CommissionedType>(
        employeeRepository, payMethodToSender, affiliationService
) {
    override fun findAllEmployees(): List<Employee<CommissionedType, *>> {
        return employeeRepository.findAllEmployeesByPayType(CommissionedType::class)
    }

    override fun calculateGross(employee: Employee<CommissionedType, *>, payPeriod: PayPeriod): BigDecimal {
        val saleRate = employee.payType.saleRate
        val receipts = salesReceiptRepository.findAllSalesReceiptsForPayPeriod(employee.getId(), payPeriod)
                .fold(BigDecimal.ZERO) {acc, sr ->
                    acc + sr.amount.multiply(BigDecimal.valueOf(saleRate))
                }
        return receipts + employee.payType.amount
    }
}