package ru.aasmc.payrolsystem.model

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import ru.aasmc.payrolsystem.dto.PayPeriod
import ru.aasmc.payrolsystem.util.isInPayPeriod
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class PaymentType(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null
) {
    abstract fun calculatePaymentFor(period: PayPeriod): BigDecimal
}

@Entity
class SalaryType(
        var amount: BigDecimal
) : PaymentType() {

    override fun calculatePaymentFor(period: PayPeriod): BigDecimal {
        return amount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val o = other as? SalaryType ?: return false
        return id != null && id == o.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "SalaryType(id=$id, amount=$amount)"
    }
}

@Entity
class HourlyType(
        var rate: BigDecimal,
        @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
        @JoinColumn(name = "time_card_id")
        @LazyCollection(value = LazyCollectionOption.EXTRA)
        var timeCards: MutableSet<TimeCard> = hashSetOf()
) : PaymentType() {

    override fun calculatePaymentFor(period: PayPeriod): BigDecimal {
        return timeCards.fold(BigDecimal.ZERO) { acc, tc ->
            if (isInPayPeriod(tc.date, period)) {
                acc + calculatePayForTimeCard(tc)
            } else {
                acc
            }
        }
    }

    private fun calculatePayForTimeCard(timeCard: TimeCard): BigDecimal {
        val overtimeHours = maxOf(0, timeCard.hours - 8)
        val normalHours = timeCard.hours - overtimeHours
        val normalResult = rate.multiply(BigDecimal.valueOf(normalHours.toLong()))
        val overtimeResult = rate.multiply(BigDecimal.valueOf(overtimeHours * 1.5))
        return normalResult + overtimeResult
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val o = other as? HourlyType ?: return false
        return id != null && id == o.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "HourlyType(id=$id, rate=$rate)"
    }
}

@Entity
class CommissionedType(
        @Column(name = "salary_amount", nullable = false)
        var salaryAmount: BigDecimal,
        @Column(name = "sale_rate", nullable = false)
        var saleRate: Double,
        @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
        @JoinColumn(name = "sale_receipt_id")
        @LazyCollection(value = LazyCollectionOption.EXTRA)
        var salesReceipts: MutableSet<SalesReceipt> = hashSetOf()
) : PaymentType() {

    override fun calculatePaymentFor(period: PayPeriod): BigDecimal {
        val receiptsTotal = salesReceipts.fold(BigDecimal.ZERO) { acc, sr ->
            if (isInPayPeriod(sr.date, period)) {
                acc + sr.amount.multiply(BigDecimal.valueOf(saleRate))
            } else {
                acc
            }
        }
        return receiptsTotal + salaryAmount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val o = other as? CommissionedType ?: return false
        return id != null && id == o.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "CommissionedType(id=$id, salaryAmount=$salaryAmount, saleRate=$saleRate)"
    }
}