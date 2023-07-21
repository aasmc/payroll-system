package ru.aasmc.payrolsystem.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.aasmc.payrolsystem.dto.*
import ru.aasmc.payrolsystem.errors.PayrollServiceError
import ru.aasmc.payrolsystem.model.*
import ru.aasmc.payrolsystem.model.mapper.CommissionedEmployeeMapper
import ru.aasmc.payrolsystem.model.mapper.EmployeeMapper
import ru.aasmc.payrolsystem.model.mapper.HourlyEmployeeMapper
import ru.aasmc.payrolsystem.model.mapper.SalaryEmployeeMapper
import ru.aasmc.payrolsystem.repository.AffiliationRepository
import ru.aasmc.payrolsystem.repository.EmployeeRepository
import ru.aasmc.payrolsystem.service.AffiliationService
import ru.aasmc.payrolsystem.service.EmployeeService

@Service
@Transactional
class EmployeeServiceImpl(
        private val employeeRepository: EmployeeRepository,
        private val affiliationRepository: AffiliationRepository,
        private val salaryEmployeeMapper: SalaryEmployeeMapper,
        private val hourlyEmployeeMapper: HourlyEmployeeMapper,
        private val commissionedEmployeeMapper: CommissionedEmployeeMapper
) : EmployeeService {

    override fun createSalaryEmployee(dto: SalaryEmployeeRequest): EmployeeResponse {
        return createAndSaveEmployee(salaryEmployeeMapper, dto)
    }

    override fun createHourlyEmployee(dto: HourlyEmployeeRequest): EmployeeResponse {
        return createAndSaveEmployee(hourlyEmployeeMapper, dto)
    }

    override fun createCommissionedEmployee(dto: CommissionedEmployeeRequest): EmployeeResponse {
        return createAndSaveEmployee(commissionedEmployeeMapper, dto)
    }

    override fun getAllEmployees(): List<EmployeeResponse> {
        return employeeRepository.findAll().map(::toDto)
    }

    override fun getEmployeeById(id: Long): EmployeeResponse {
        val employee = getEmployeeByIdOrThrow(id)
        return toDto(employee)
    }

    override fun addTimeCard(dto: TimeCardRequest) {
        val employee = getEmployeeByIdOrThrow(dto.employeeId)
        updateEmployee(employee, this::checkEmployeeIsHourly) { empl ->
            val payType = empl.paymentType as HourlyType
            val timeCard = TimeCard(
                    date = dto.date,
                    hours = dto.hours,
            )
            payType.timeCards.add(timeCard)
        }
    }

    private fun checkEmployeeIsHourly(employee: Employee) {
        if (employee.paymentType !is HourlyType) {
            throw PayrollServiceError(400, "Cannot create a time card for employee " +
                    "with id=${employee.id}, because the employee is not Hourly.")
        }
    }

    override fun addSalesReceipt(dto: SalesReceiptRequest) {
        val employee = getEmployeeByIdOrThrow(dto.employeeId)
        updateEmployee(
                employee,
                this::checkEmployeeIsCommissioned
        ) { empl ->
            val payType = empl.paymentType as CommissionedType
            val salesReceipt = SalesReceipt(
                    date = dto.date,
                    amount = dto.amount
            )
            payType.salesReceipts.add(salesReceipt)
        }
    }

    override fun addToAffiliation(employeeId: Long, affiliationId: Long) {
        val employee = getEmployeeByIdOrThrow(employeeId)
        val affiliation = affiliationRepository.findById(affiliationId)
                .orElseThrow {
                    PayrollServiceError(404, "Affiliation with ID=$affiliationId not found.")
                }
        employee.affiliations.add(affiliation)
        employeeRepository.save(employee)
    }

    private fun checkEmployeeIsCommissioned(employee: Employee) {
        if (employee.paymentType !is CommissionedType) {
            throw PayrollServiceError(400, "Cannot create a sales receipt for employee " +
                    "with id=${employee.id}, because the employee is not Commissioned.")
        }
    }

    private fun updateEmployee(employee: Employee,
                               check: ((employee: Employee) -> Unit)? = null,
                               update: (employee: Employee) -> Unit): Employee {
        check?.invoke(employee)
        update(employee)
        return employeeRepository.save(employee)
    }

    fun toDto(employee: Employee): EmployeeResponse {
        return EmployeeResponse(
                id = employee.id!!,
                name = employee.name,
                address = employee.address
        )
    }

    private fun <T : BaseEmployeeRequest> createAndSaveEmployee(mapper: EmployeeMapper<T>,
                                                                dto: T): EmployeeResponse {
        val domain = mapper.mapToDomain(dto)
        val saved = employeeRepository.save(domain)
        return mapper.mapToDto(saved)
    }

    private fun getEmployeeByIdOrThrow(id: Long): Employee {
        return employeeRepository.findById(id)
                .orElseThrow {
                    PayrollServiceError(404, "Employee with ID=$id not found!")
                }
    }
}