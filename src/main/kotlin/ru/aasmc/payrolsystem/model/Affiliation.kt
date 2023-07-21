package ru.aasmc.payrolsystem.model

import ru.aasmc.payrolsystem.dto.PayPeriod
import ru.aasmc.payrolsystem.util.isInPayPeriod
import java.math.BigDecimal
import java.time.DayOfWeek
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Affiliation(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @ManyToMany(mappedBy = "affiliations")
        var members: MutableSet<Employee> = hashSetOf()
) {
    abstract fun calculateDeductions(period: PayPeriod, serviceCharges: List<ServiceCharge>): BigDecimal
}

@Entity
class UnionAffiliation(
        var dues: BigDecimal
) : Affiliation() {

    override fun calculateDeductions(period: PayPeriod, serviceCharges: List<ServiceCharge>): BigDecimal {
        val numFridays = numFridaysInPeriod(period)
        val afterDues = dues.multiply(BigDecimal.valueOf(numFridays.toLong()))
        val charges = serviceCharges.fold(BigDecimal.ZERO) { acc, sc ->
            if (isInPayPeriod(sc.date, period)) {
                acc + sc.amount
            } else {
                acc
            }
        }
        return afterDues + charges
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


