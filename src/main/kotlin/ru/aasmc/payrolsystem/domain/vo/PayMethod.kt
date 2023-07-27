package ru.aasmc.payrolsystem.domain.vo

import ru.aasmc.payrolsystem.domain.common.ValueObject
import ru.aasmc.payrolsystem.domain.interfaces.PayCheckSender
import javax.persistence.Embeddable
import javax.persistence.MappedSuperclass

@MappedSuperclass
sealed class PayMethod: ValueObject {
}

@Embeddable
data class MailMethod(
        val postalAddress: String
): PayMethod() {

}

@Embeddable
class Paymaster: PayMethod() {
    override fun toString(): String {
        return "Paymaster PayMethod."
    }
}

@Embeddable
data class DepositMethod(
        val bankAccount: String
): PayMethod()