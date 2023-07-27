package ru.aasmc.payrolsystem.domain.vo

import ru.aasmc.payrolsystem.domain.common.PersistableObject
import ru.aasmc.payrolsystem.domain.common.ValueObject
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
class ServiceCharge(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        var employeeId: String,
        var amount: BigDecimal,
        var date: LocalDate,
        var affiliationId: Long
): ValueObject, PersistableObject<Long> {
    override fun getId(): Long {
        return id ?: -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val o = other as? ServiceCharge ?: return false
        return id != null && id == o.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "ServiceCharge(id=$id, amount=$amount, dat=$date, affiliationId=$affiliationId, employeeId=$employeeId)"
    }
}