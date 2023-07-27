package ru.aasmc.payrolsystem.domain.vo

import ru.aasmc.payrolsystem.domain.common.PersistableObject
import ru.aasmc.payrolsystem.domain.common.ValueObject
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class SalesReceipt(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        var date: LocalDate,
        var amount: BigDecimal,
        var employeeId: String
): ValueObject, PersistableObject<Long> {
    override fun getId(): Long {
        return id ?: -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val o = other as? SalesReceipt ?: return false
        return id != null && id == o.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "SalesReceipt(id=$id, date=$date, amount=$amount, employeeId=$employeeId)"
    }
}