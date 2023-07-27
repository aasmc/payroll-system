package ru.aasmc.payrolsystem.domain.service.impl

import ru.aasmc.payrolsystem.domain.entity.Employee
import ru.aasmc.payrolsystem.domain.interfaces.EmployeeRepository
import ru.aasmc.payrolsystem.domain.interfaces.PayCheckSender
import ru.aasmc.payrolsystem.domain.service.AffiliationService
import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.SalaryType
import java.math.BigDecimal
import kotlin.reflect.KClass

class SalaryPayRollService(
        employeeRepository: EmployeeRepository,
        payMethodToSender: Map<KClass<*>, PayCheckSender>,
        affiliationService: AffiliationService
) : BasePayRollService<SalaryType>(employeeRepository, payMethodToSender, affiliationService) {
    override fun findAllEmployees(): List<Employee<SalaryType, *>> {
        return employeeRepository.findAllEmployeesByPayType(SalaryType::class)
    }

    override fun calculateGross(employee: Employee<SalaryType, *>, payPeriod: PayPeriod): BigDecimal {
        return employee.payType.amount
    }
}