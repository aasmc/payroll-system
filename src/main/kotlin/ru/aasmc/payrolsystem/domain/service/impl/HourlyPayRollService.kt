package ru.aasmc.payrolsystem.domain.service.impl

import ru.aasmc.payrolsystem.domain.entity.Employee
import ru.aasmc.payrolsystem.domain.interfaces.EmployeeRepository
import ru.aasmc.payrolsystem.domain.interfaces.PayCheckSender
import ru.aasmc.payrolsystem.domain.interfaces.TimeCardRepository
import ru.aasmc.payrolsystem.domain.service.AffiliationService
import ru.aasmc.payrolsystem.domain.vo.HourlyType
import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.TimeCard
import ru.aasmc.payrolsystem.util.isInPayPeriod
import java.math.BigDecimal
import kotlin.reflect.KClass

class HourlyPayRollService(
        employeeRepository: EmployeeRepository,
        payMethodToSender: Map<KClass<*>, PayCheckSender>,
        affiliationService: AffiliationService,
        private val timeCardRepository: TimeCardRepository
) : BasePayRollService<HourlyType>(
        employeeRepository, payMethodToSender, affiliationService
) {
    override fun findAllEmployees(): List<Employee<HourlyType, *>> {
        return employeeRepository.findAllEmployeesByPayType(HourlyType::class)
    }

    override fun calculateGross(employee: Employee<HourlyType, *>, payPeriod: PayPeriod): BigDecimal {
        val timeCards = timeCardRepository.findAllTimeCardsForPayPeriod(employee.getId(), payPeriod)
        return timeCards.fold(BigDecimal.ZERO) { acc, tc ->
            if (isInPayPeriod(tc.date, payPeriod)) {
                acc + calculatePayForTimeCard(tc, employee.payType)
            } else {
                acc
            }
        }
    }

    private fun calculatePayForTimeCard(timeCard: TimeCard, type: HourlyType): BigDecimal {
        val overtimeHours = maxOf(0, timeCard.hours - 8)
        val normalHours = timeCard.hours - overtimeHours
        val normalResult = type.ratePerHour.multiply(BigDecimal.valueOf(normalHours.toLong()))
        val overtimeResult = type.ratePerHour.multiply(BigDecimal.valueOf(overtimeHours * type.overtimeRate))
        return normalResult + overtimeResult
    }
}