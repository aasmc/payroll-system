package ru.aasmc.payrolsystem.model.mapper

import org.springframework.stereotype.Component
import ru.aasmc.payrolsystem.dto.HourlyEmployeeRequest
import ru.aasmc.payrolsystem.model.Employee
import ru.aasmc.payrolsystem.model.HourlyType
import ru.aasmc.payrolsystem.model.PaymentSchedule

@Component
class HourlyEmployeeMapper(
        private val payMethodMapper: PayMethodMapper
): EmployeeMapper<HourlyEmployeeRequest> {

    override fun mapToDomain(dto: HourlyEmployeeRequest): Employee {
        val payMethod = payMethodMapper.mapPaymentMethod(dto)
        val schedule = PaymentSchedule.WEEKLY
        val payType = HourlyType(dto.rate)
        return Employee(
                name = dto.name,
                address = dto.address,
                paymentMethod = payMethod,
                paymentSchedule = schedule,
                paymentType = payType
        )
    }
}