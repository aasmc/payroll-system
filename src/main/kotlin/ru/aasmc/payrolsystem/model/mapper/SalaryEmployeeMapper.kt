package ru.aasmc.payrolsystem.model.mapper

import org.springframework.stereotype.Component
import ru.aasmc.payrolsystem.dto.SalaryEmployeeRequest
import ru.aasmc.payrolsystem.model.Employee
import ru.aasmc.payrolsystem.model.PaymentSchedule
import ru.aasmc.payrolsystem.model.SalaryType

@Component
class SalaryEmployeeMapper(
        private val paymentMethodMapper: PayMethodMapper
): EmployeeMapper<SalaryEmployeeRequest> {

    override fun mapToDomain(dto: SalaryEmployeeRequest): Employee {
        val payMethod = paymentMethodMapper.mapPaymentMethod(dto)
        val schedule = PaymentSchedule.MONTHLY
        val payType = SalaryType(dto.salary)
        return Employee(
                name = dto.name,
                address = dto.address,
                paymentMethod = payMethod,
                paymentSchedule = schedule,
                paymentType = payType
        )
    }
}