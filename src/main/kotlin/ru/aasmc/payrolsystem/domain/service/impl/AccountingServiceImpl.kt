package ru.aasmc.payrolsystem.domain.service.impl

import ru.aasmc.payrolsystem.domain.interfaces.SalesReceiptRepository
import ru.aasmc.payrolsystem.domain.interfaces.ServiceChargeRepository
import ru.aasmc.payrolsystem.domain.interfaces.TimeCardRepository
import ru.aasmc.payrolsystem.domain.service.AccountingService
import ru.aasmc.payrolsystem.domain.vo.SalesReceipt
import ru.aasmc.payrolsystem.domain.vo.ServiceCharge
import ru.aasmc.payrolsystem.domain.vo.TimeCard

class AccountingServiceImpl(
        private val timeCardRepository: TimeCardRepository,
        private val salesReceiptRepository: SalesReceiptRepository,
        private val serviceChargeRepository: ServiceChargeRepository
): AccountingService {
    override fun acceptTimeCard(timeCard: TimeCard) {
        timeCardRepository.save(timeCard)
    }

    override fun acceptSalesReceipt(salesReceipt: SalesReceipt) {
        salesReceiptRepository.save(salesReceipt)
    }

    override fun acceptServiceCharge(serviceCharge: ServiceCharge) {
        serviceChargeRepository.save(serviceCharge)
    }
}