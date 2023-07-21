package ru.aasmc.payrolsystem.service

import ru.aasmc.payrolsystem.dto.ServiceChargeRequest
import ru.aasmc.payrolsystem.dto.UnionAffiliationDto

interface AffiliationService {

    fun getAllUnionAffiliations(): List<UnionAffiliationDto>

    fun createUnionAffiliation(dto: UnionAffiliationDto): UnionAffiliationDto

    fun createServiceCharge(dto: ServiceChargeRequest)
}