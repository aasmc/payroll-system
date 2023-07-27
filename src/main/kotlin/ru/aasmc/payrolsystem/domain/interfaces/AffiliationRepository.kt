package ru.aasmc.payrolsystem.domain.interfaces

import ru.aasmc.payrolsystem.domain.entity.Affiliation
import java.util.*

interface AffiliationRepository {
    fun createAffiliation(affiliation: Affiliation)
    fun findAffiliationById(id: Long): Optional<Affiliation>
    fun findAllAffiliationsOfEmployee(employeeId: String): List<Affiliation>
}