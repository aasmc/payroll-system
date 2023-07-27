package ru.aasmc.payrolsystem.domain.service

import ru.aasmc.payrolsystem.domain.vo.SalesReceipt
import ru.aasmc.payrolsystem.domain.vo.ServiceCharge
import ru.aasmc.payrolsystem.domain.vo.TimeCard

interface AccountingService {

    fun acceptTimeCard(timeCard: TimeCard)

    fun acceptSalesReceipt(salesReceipt: SalesReceipt)

    fun acceptServiceCharge(serviceCharge: ServiceCharge)

}