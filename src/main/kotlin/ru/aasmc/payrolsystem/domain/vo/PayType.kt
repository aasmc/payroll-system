package ru.aasmc.payrolsystem.domain.vo

import ru.aasmc.payrolsystem.domain.common.ValueObject
import java.math.BigDecimal
import javax.persistence.Embeddable
import javax.persistence.MappedSuperclass

@MappedSuperclass
sealed class PayType: ValueObject

@Embeddable
data class SalaryType(
        val amount: BigDecimal
): PayType()

@Embeddable
data class HourlyType(
        val ratePerHour: BigDecimal,
        val overtimeRate: Double
): PayType()

@Embeddable
data class CommissionedType(
        val amount: BigDecimal,
        val saleRate: Double
): PayType()