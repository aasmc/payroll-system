package ru.aasmc.payrolsystem.model.mapper

import org.springframework.stereotype.Component
import ru.aasmc.payrolsystem.dto.BaseEmployeeRequest
import ru.aasmc.payrolsystem.dto.PayMethod
import ru.aasmc.payrolsystem.errors.PayrollServiceError
import ru.aasmc.payrolsystem.model.DepositMethod
import ru.aasmc.payrolsystem.model.MailPayment
import ru.aasmc.payrolsystem.model.PaymasterMethod
import ru.aasmc.payrolsystem.model.PaymentMethod

@Component
class PayMethodMapper {
    fun mapPaymentMethod(dto: BaseEmployeeRequest): PaymentMethod {
        return when(dto.payMethod) {
            PayMethod.MAIL -> MailPayment(dto.address)
            PayMethod.DEPOSIT -> DepositMethod(
                    dto.bankAccount
                            ?: throw PayrollServiceError(400, "Cannot create Deposit Payment Method without bank account."))
            PayMethod.PAYMASTER -> PaymasterMethod(dto.name)
        }
    }
}