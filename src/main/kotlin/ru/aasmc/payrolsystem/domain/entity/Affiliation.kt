package ru.aasmc.payrolsystem.domain.entity

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import ru.aasmc.payrolsystem.domain.service.AccountingService
import ru.aasmc.payrolsystem.domain.vo.PayPeriod
import ru.aasmc.payrolsystem.domain.vo.ServiceCharge
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Affiliation(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @ElementCollection(fetch = FetchType.LAZY)
        @LazyCollection(LazyCollectionOption.EXTRA)
        var members: MutableSet<String> = hashSetOf()
) : ru.aasmc.payrolsystem.domain.common.Entity<Affiliation, Long> {
    override fun getId(): Long {
        return id ?: -1
    }

    fun registerMember(memberId: String) {
        members.add(memberId)
    }

    fun deregisterMember(memberId: String) {
        members.remove(memberId)
    }

    fun isMemberOfAffiliation(employeeId: String): Boolean {
        return members.contains(employeeId)
    }

    fun serviceChargeOf(employeeId: String, amount: BigDecimal, date: LocalDate): ServiceCharge =
            ServiceCharge(employeeId = employeeId, amount = amount, date = date, affiliationId = this.getId())

    fun submitServiceCharge(serviceCharge: ServiceCharge, accountingService: AccountingService) {
        accountingService.acceptServiceCharge(serviceCharge)
    }

    abstract fun calculateDeductions(period: PayPeriod): BigDecimal

}

@Entity
class UnionAffiliation(
        var dues: BigDecimal
) : Affiliation() {
    override fun calculateDeductions(period: PayPeriod): BigDecimal {
        val numFridays = numFridaysInPeriod(period)
        return dues.multiply(BigDecimal.valueOf(numFridays.toLong()))
    }

    private fun numFridaysInPeriod(period: PayPeriod): Int {
        var fridays = 0
        var start = period.start
        while (start.isBefore(period.end.plusDays(1))) {
            if (start.dayOfWeek == DayOfWeek.FRIDAY) {
                ++fridays
            }
            start = start.plusDays(1)
        }
        return fridays
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val o = other as? UnionAffiliation ?: return false
        return id != null && id == o.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "UnionAffiliation(id=$id, dues=$dues)"
    }
}