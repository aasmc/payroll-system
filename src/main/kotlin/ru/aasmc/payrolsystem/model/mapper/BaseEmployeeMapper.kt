package ru.aasmc.payrolsystem.model.mapper

import ru.aasmc.payrolsystem.dto.BaseEmployeeRequest
import ru.aasmc.payrolsystem.dto.EmployeeResponse
import ru.aasmc.payrolsystem.model.Employee

interface EmployeeMapper<Dto: BaseEmployeeRequest> {

    fun mapToDomain(dto: Dto): Employee

    fun mapToDto(domain: Employee): EmployeeResponse {
        return EmployeeResponse(
                id = domain.id!!,
                name = domain.name,
                address = domain.address
        )
    }
}