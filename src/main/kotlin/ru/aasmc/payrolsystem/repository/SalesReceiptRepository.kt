package ru.aasmc.payrolsystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.aasmc.payrolsystem.model.SalesReceipt

interface SalesReceiptRepository: JpaRepository<SalesReceipt, Long> {
}