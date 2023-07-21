package ru.aasmc.payrolsystem.model

import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
class ServiceCharge(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        var employee: Employee,
        var amount: BigDecimal,
        var date: LocalDate,
        @ManyToOne
        @JoinColumn(name = "affiliation_id")
        var affiliation: Affiliation
){
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                val o = other as? ServiceCharge ?: return false
                return id != null && id == o.id
        }

        override fun hashCode(): Int {
                return javaClass.hashCode()
        }

        override fun toString(): String {
                return "ServiceCharge(id=$id, amount=$amount)"
        }
}