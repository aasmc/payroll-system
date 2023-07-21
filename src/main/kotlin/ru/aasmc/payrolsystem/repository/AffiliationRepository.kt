package ru.aasmc.payrolsystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.aasmc.payrolsystem.model.Affiliation
import ru.aasmc.payrolsystem.model.UnionAffiliation

interface AffiliationRepository : JpaRepository<Affiliation, Long> {

    @Query("select u from UnionAffiliation u left join fetch u.members")
    fun findAllUnionAffiliations(): List<UnionAffiliation>

}