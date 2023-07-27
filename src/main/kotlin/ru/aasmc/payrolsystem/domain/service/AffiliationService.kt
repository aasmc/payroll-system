package ru.aasmc.payrolsystem.domain.service

import ru.aasmc.payrolsystem.domain.entity.Affiliation
import ru.aasmc.payrolsystem.domain.vo.ServiceCharge

interface AffiliationService {

    fun addMember(memberId: String, affiliationId: Long)

    fun removeMember(memberId: String, affiliationId: Long)

    fun findAllAffiliationsOfEmployee(employeeId: String): List<Affiliation>

    fun findAllUnpaidServiceChargesOfEmployee(employeeId: String): List<ServiceCharge>

}