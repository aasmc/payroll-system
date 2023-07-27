package ru.aasmc.payrolsystem.domain.interfaces

import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.TimeCard

interface TimeCardRepository {
    fun save(timeCard: TimeCard)

    fun findAllTimeCardsForPayPeriod(employeeId: String, payPeriod: PayPeriod): List<TimeCard>
}