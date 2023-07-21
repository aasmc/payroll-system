package ru.aasmc.payrolsystem.service

import ru.aasmc.payrolsystem.dto.*

interface EmployeeService {

    fun createSalaryEmployee(dto: SalaryEmployeeRequest): EmployeeResponse

    fun createHourlyEmployee(dto: HourlyEmployeeRequest): EmployeeResponse

    fun createCommissionedEmployee(dto: CommissionedEmployeeRequest): EmployeeResponse

    fun getAllEmployees(): List<EmployeeResponse>

    fun getEmployeeById(id: Long): EmployeeResponse

    fun addTimeCard(dto: TimeCardRequest)

    fun addSalesReceipt(dto: SalesReceiptRequest)

    fun addToAffiliation(employeeId: Long, affiliationId: Long)

}