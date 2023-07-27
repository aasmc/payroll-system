package ru.aasmc.payrolsystem.domain.interfaces

import ru.aasmc.payrolsystem.domain.vo.ServiceCharge

interface ServiceChargeRepository {

    fun save(serviceCharge: ServiceCharge)
    fun findAllUnpaidServiceChargesOfEmployee(employeeId: String): List<ServiceCharge>

}