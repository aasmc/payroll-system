package ru.aasmc.payrolsystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.aasmc.payrolsystem.model.Employee

interface EmployeeRepository: JpaRepository<Employee, Long> {

    fun findAllByIdIn(ids: List<Long>): List<Employee>

}