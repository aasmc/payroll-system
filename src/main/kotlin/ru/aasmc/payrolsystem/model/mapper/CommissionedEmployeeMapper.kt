package ru.aasmc.payrolsystem.model.mapper

import org.springframework.stereotype.Component
import ru.aasmc.payrolsystem.dto.CommissionedEmployeeRequest
import ru.aasmc.payrolsystem.model.CommissionedType
import ru.aasmc.payrolsystem.model.Employee
import ru.aasmc.payrolsystem.model.PaymentSchedule

@Component
class CommissionedEmployeeMapper(
        private val payMethodMapper: PayMethodMapper
): EmployeeMapper<CommissionedEmployeeRequest> {
    override fun mapToDomain(dto: CommissionedEmployeeRequest): Employee {
        val payMethod = payMethodMapper.mapPaymentMethod(dto)
        val schedule = PaymentSchedule.BIWEEKLY
        val payType = CommissionedType(dto.salary, dto.commissionRate)
        return Employee(
                name = dto.name,
                address = dto.address,
                paymentMethod = payMethod,
                paymentSchedule = schedule,
                paymentType = payType
        )
    }
}