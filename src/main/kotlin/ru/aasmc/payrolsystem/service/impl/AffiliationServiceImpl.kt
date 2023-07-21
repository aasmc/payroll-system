package ru.aasmc.payrolsystem.service.impl

import org.springframework.stereotype.Service
import ru.aasmc.payrolsystem.dto.ServiceChargeRequest
import ru.aasmc.payrolsystem.dto.UnionAffiliationDto
import ru.aasmc.payrolsystem.errors.PayrollServiceError
import ru.aasmc.payrolsystem.model.ServiceCharge
import ru.aasmc.payrolsystem.model.UnionAffiliation
import ru.aasmc.payrolsystem.repository.AffiliationRepository
import ru.aasmc.payrolsystem.repository.EmployeeRepository
import ru.aasmc.payrolsystem.repository.ServiceChargeRepository
import ru.aasmc.payrolsystem.service.AffiliationService

@Service
class AffiliationServiceImpl(
        private val affiliationRepository: AffiliationRepository,
        private val employeeRepository: EmployeeRepository,
        private val serviceChargeRepository: ServiceChargeRepository
) : AffiliationService {
    override fun getAllUnionAffiliations(): List<UnionAffiliationDto> {
        return affiliationRepository.findAllUnionAffiliations().map { union ->
            mapToDto(union)
        }
    }

    override fun createUnionAffiliation(dto: UnionAffiliationDto): UnionAffiliationDto {
        val saved = affiliationRepository.save(mapToDomain(dto))
        return mapToDto(saved)
    }

    override fun createServiceCharge(dto: ServiceChargeRequest) {
        val employee = employeeRepository.findById(dto.employeeId)
                .orElseThrow {
                    PayrollServiceError(404, "Employee with ID=${dto.employeeId} not found.")
                }
        val affiliation = affiliationRepository.findById(dto.affiliationId)
                .orElseThrow {
                    PayrollServiceError(404, "Affiliation with ID=${dto.affiliationId} not found.")
                }
        val charge = ServiceCharge(
                employee = employee,
                amount = dto.amount,
                date = dto.date,
                affiliation = affiliation
        )
        serviceChargeRepository.save(charge)
    }

    private fun mapToDto(union: UnionAffiliation) = UnionAffiliationDto(
            id = union.id,
            dues = union.dues,
            memberIds = union.members.mapNotNull { it.id }
    )

    private fun mapToDomain(dto: UnionAffiliationDto): UnionAffiliation {
        return UnionAffiliation(
                dues = dto.dues
        )
    }
}