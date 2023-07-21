package ru.aasmc.payrolsystem.testutil

import ru.aasmc.payrolsystem.dto.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

fun getSalesReceiptDto(employeeId: Long, amount: BigDecimal = BigDecimal.TEN): SalesReceiptRequest {
    return SalesReceiptRequest(
            date = LocalDate.of(2023, Month.JULY, 21),
            amount = amount,
            employeeId = employeeId
    )
}

fun getTimeCardDto(employeeId: Long, hours: Int): TimeCardRequest {
    return TimeCardRequest(
            date = LocalDate.of(2023, Month.JULY, 21),
            hours = hours,
            employeeId = employeeId
    )
}

fun getUnionAffiliationDto(): UnionAffiliationDto {
    return UnionAffiliationDto(
            dues = BigDecimal.ONE
    )
}

fun getServiceChargeDto(employeeId: Long, affiliationId: Long): ServiceChargeRequest {
    return ServiceChargeRequest(
            date = LocalDate.of(2023, Month.JULY, 21),
            amount = BigDecimal.ONE,
            employeeId = employeeId,
            affiliationId = affiliationId
    )
}

fun getSalaryEmployeeDto(): SalaryEmployeeRequest {
    return SalaryEmployeeRequest(
            name = "UserName",
            payMethod = PayMethod.PAYMASTER,
            address = "UserAddress",
            bankAccount = "Bank",
            salary = BigDecimal.TEN
    )
}

fun getHourlyEmployeeDto(): HourlyEmployeeRequest {
    return HourlyEmployeeRequest(
            name = "UserName",
            payMethod = PayMethod.PAYMASTER,
            address = "UserAddress",
            bankAccount = "Bank",
            rate = BigDecimal.ONE
    )
}

fun getCommissionedEmployeeDto(): CommissionedEmployeeRequest {
    return CommissionedEmployeeRequest(
            name = "UserName",
            payMethod = PayMethod.PAYMASTER,
            address = "UserAddress",
            bankAccount = "Bank",
            salary = BigDecimal.valueOf(5L),
            commissionRate = 0.25
    )
}