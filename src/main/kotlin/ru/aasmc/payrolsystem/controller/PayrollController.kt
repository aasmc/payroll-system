package ru.aasmc.payrolsystem.controller

import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.aasmc.payrolsystem.dto.PayCheckResponse
import ru.aasmc.payrolsystem.service.PayrollService
import java.time.LocalDate

private val log = LoggerFactory.getLogger(PayrollController::class.java)

private const val DATE_PATTERN = "yyyy-MM-dd"

@RestController
@RequestMapping("/payroll")
class PayrollController(
        private val payrollService: PayrollService
) {

    @GetMapping
    fun getPayChecks(@RequestParam("date") @DateTimeFormat(pattern = DATE_PATTERN) date: LocalDate): PayCheckResponse {
        log.info("Received request to GET paychecks for date {}", date)
        return payrollService.makePay(date)
    }

}