package ru.aasmc.payrolsystem.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.aasmc.payrolsystem.model.ServiceCharge
import java.time.LocalDate

interface ServiceChargeRepository: JpaRepository<ServiceCharge, Long> {

    fun findAllByEmployee_IdAndDateBetweenAndAffiliation_IdIn(employeeId: Long,
                                                             start: LocalDate,
                                                             end: LocalDate,
                                                             affiliationIds: Set<Long>): List<ServiceCharge>

}