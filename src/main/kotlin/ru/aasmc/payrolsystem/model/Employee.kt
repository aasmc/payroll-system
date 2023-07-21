package ru.aasmc.payrolsystem.model

import java.time.LocalDate
import javax.persistence.*

@Entity
class Employee(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        var name: String,
        var address: String,
        @Embedded
        var paymentMethod: PaymentMethod,
        @Enumerated(EnumType.STRING)
        var paymentSchedule: PaymentSchedule,
        @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE], optional = false)
        @JoinColumn(name = "payment_type_id", nullable = false, unique = true)
        var paymentType: PaymentType,
        @Column(name = "last_pay_day")
        var lastPayDay: LocalDate? = null,
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "employee_affiliation",
                joinColumns = [
                    JoinColumn(name = "employee_id", nullable = false)
                ],
                inverseJoinColumns = [
                    JoinColumn(name = "affiliation_id", nullable = false)
                ]
        )
        var affiliations: MutableSet<Affiliation> = hashSetOf(),
) {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                val o = other as? Employee ?: return false
                return id != null && id == o.id
        }

        override fun hashCode(): Int {
                return javaClass.hashCode()
        }

        override fun toString(): String {
                return "Employee(id=$id, name='$name', address='$address', paymentSchedule=$paymentSchedule)"
        }


}