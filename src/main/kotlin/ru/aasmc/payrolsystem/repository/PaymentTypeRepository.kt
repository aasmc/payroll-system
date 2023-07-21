package ru.aasmc.payrolsystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.aasmc.payrolsystem.model.PaymentType

interface PaymentTypeRepository: JpaRepository<PaymentType, Long> {
}