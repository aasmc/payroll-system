package ru.aasmc.payrolsystem.dto

import java.math.BigDecimal

open class BaseEmployeeRequest(
        open val name: String,
        open val address: String,
        open val payMethod: PayMethod,
        open val bankAccount: String? = null,
        open val id: Long? = null
)

open class SalaryEmployeeRequest(
        override val name: String,
        override val address: String,
        override val payMethod: PayMethod,
        override val bankAccount: String? = null,
        val salary: BigDecimal,
) : BaseEmployeeRequest(name, address, payMethod, bankAccount)

open class HourlyEmployeeRequest(
        override val name: String,
        override val address: String,
        override val payMethod: PayMethod,
        override val bankAccount: String? = null,
        val rate: BigDecimal,
) : BaseEmployeeRequest( name, address, payMethod, bankAccount)

open class CommissionedEmployeeRequest(
        override val name: String,
        override val address: String,
        override val payMethod: PayMethod,
        override val bankAccount: String? = null,
        val salary: BigDecimal,
        val commissionRate: Double,
) : BaseEmployeeRequest(name, address, payMethod, bankAccount)

data class EmployeeResponse(
        val id: Long,
        val name: String,
        val address: String
)
