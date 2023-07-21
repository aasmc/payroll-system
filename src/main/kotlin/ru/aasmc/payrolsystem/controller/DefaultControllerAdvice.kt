package ru.aasmc.payrolsystem.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.aasmc.payrolsystem.errors.ErrorResponse
import ru.aasmc.payrolsystem.errors.PayrollServiceError

private val log = LoggerFactory.getLogger(DefaultControllerAdvice::class.java)

@RestControllerAdvice
class DefaultControllerAdvice {

    @ExceptionHandler(PayrollServiceError::class)
    fun handlePayrollException(ex: PayrollServiceError): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.valueOf(ex.code)
        log.error("Handling PayrollServiceError with message: {}, status: {}", ex.message, status)
        val resp = ErrorResponse(message = ex.message.orEmpty(), code = ex.code)
        return ResponseEntity(resp, status)
    }

}