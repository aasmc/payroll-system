package ru.aasmc.payrolsystem.domain.service.impl

import ru.aasmc.payrolsystem.domain.entity.Affiliation
import ru.aasmc.payrolsystem.domain.interfaces.AffiliationRepository
import ru.aasmc.payrolsystem.domain.interfaces.ServiceChargeRepository
import ru.aasmc.payrolsystem.domain.service.AffiliationService
import ru.aasmc.payrolsystem.domain.vo.ServiceCharge
import ru.aasmc.payrolsystem.errors.PayrollServiceError

class AffiliationServiceImpl(
        private val affiliationRepository: AffiliationRepository,
        private val serviceChargeRepository: ServiceChargeRepository
) : AffiliationService {
    override fun addMember(memberId: String, affiliationId: Long) {
        val affiliation = getAffiliationByIdOrThrow(affiliationId)
        affiliation.registerMember(memberId)
    }

    override fun removeMember(memberId: String, affiliationId: Long) {
        val affiliation = getAffiliationByIdOrThrow(affiliationId)
        affiliation.deregisterMember(memberId)
    }

    override fun findAllAffiliationsOfEmployee(employeeId: String): List<Affiliation> {
        return affiliationRepository.findAllAffiliationsOfEmployee(employeeId)
    }

    override fun findAllUnpaidServiceChargesOfEmployee(employeeId: String): List<ServiceCharge> {
        return serviceChargeRepository.findAllUnpaidServiceChargesOfEmployee(employeeId)
    }

    private fun getAffiliationByIdOrThrow(affiliationId: Long): Affiliation =
            affiliationRepository.findAffiliationById(affiliationId)
                    .orElseThrow {
                        PayrollServiceError(404, "Affiliation with ID=$affiliationId not found")
                    }
}