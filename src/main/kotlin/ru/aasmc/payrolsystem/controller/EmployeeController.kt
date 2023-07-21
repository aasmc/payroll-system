package ru.aasmc.payrolsystem.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.aasmc.payrolsystem.dto.*
import ru.aasmc.payrolsystem.service.EmployeeService

private val log = LoggerFactory.getLogger(EmployeeController::class.java)

@RestController
@RequestMapping("/employees")
class EmployeeController(
        private val employeeService: EmployeeService
) {

    @PatchMapping("/affiliation/{employeeId}/{affiliationId}")
    fun addEmployeeToAffiliation(@PathVariable("employeeId") employeeId: Long,
                                 @PathVariable("affiliationId") affiliationId: Long) {
        log.info("Received PATCH request to add Employee with id={} to Affiliation with id={}", employeeId, affiliationId)
        employeeService.addToAffiliation(employeeId, affiliationId)
    }

    @PostMapping("/salesreceipt")
    fun createSalesReceipt(@RequestBody dto: SalesReceiptRequest) {
        log.info("Received POST request to create SalesReceipt {}", dto)
        employeeService.addSalesReceipt(dto)
    }

    @PostMapping("/timecard")
    fun createTimeCard(@RequestBody dto: TimeCardRequest) {
        log.info("Received POST request to create TimeCard {}", dto)
        employeeService.addTimeCard(dto)
    }

    @GetMapping("/{employeeId}")
    fun getEmployeeById(@PathVariable("employeeId") id: Long): EmployeeResponse {
        log.info("Received request to GET employee by id {}", id)
        return employeeService.getEmployeeById(id)
    }

    @GetMapping
    fun getAllEmployees(): List<EmployeeResponse> {
        log.info("Received request to GET all employees.")
        return employeeService.getAllEmployees()
    }

    @PostMapping("/salary")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSalaryEmployee(@RequestBody dto: SalaryEmployeeRequest): EmployeeResponse {
        log.info("Received POST request to create a SalaryEmployee {}", dto)
        return employeeService.createSalaryEmployee(dto)
    }

    @PostMapping("/hourly")
    @ResponseStatus(HttpStatus.CREATED)
    fun createHourlyEmployee(@RequestBody dto: HourlyEmployeeRequest): EmployeeResponse {
        log.info("Received POST request to create an HourlyEmployee {}", dto)
        return employeeService.createHourlyEmployee(dto)
    }

    @PostMapping("/commissioned")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCommissionedEmployee(@RequestBody dto: CommissionedEmployeeRequest): EmployeeResponse {
        log.info("Received POST request to create a CommissionedEmployee {}", dto)
        return employeeService.createCommissionedEmployee(dto)
    }

}