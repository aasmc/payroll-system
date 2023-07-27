package ru.aasmc.payrolsystem.domain.interfaces

import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.SalesReceipt

interface SalesReceiptRepository {
    fun save(salesReceipt: SalesReceipt)

    fun findAllSalesReceiptsForPayPeriod(employeeId: String, payPeriod: PayPeriod): List<SalesReceipt>
}