package ru.aasmc.payrolsystem.model

import javax.persistence.*


@MappedSuperclass
abstract class PaymentMethod {

}

@Embeddable
data class MailPayment(
        @Column(name = "postal_address")
        var postalAddress: String
): PaymentMethod()

@Embeddable
class PaymasterMethod(
        var name: String
): PaymentMethod()

@Embeddable
class DepositMethod(
        @Column(name = "bank_account", nullable = false)
        var bankAccount: String
): PaymentMethod()