package ru.aasmc.payrolsystem.domain.interfaces

import ru.aasmc.payrolsystem.domain.entity.Employee
import ru.aasmc.payrolsystem.domain.vo.PayType
import java.util.*
import kotlin.reflect.KClass

interface EmployeeRepository {

    fun saveEmployee(employee: Employee<*, *>)

    fun <T: PayType> findAllEmployeesByPayType(payType: KClass<T>): List<Employee<T, *>>

    fun findEmployeeById(id: String): Optional<Employee<*, *>>

}