package ru.aasmc.payrolsystem.domain.entity

import ru.aasmc.payrolsystem.domain.service.AccountingService
import ru.aasmc.payrolsystem.domain.service.AffiliationService
import ru.aasmc.payrolsystem.domain.vo.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Employee<PT : PayType, PM: PayMethod>(
        var name: String,
        var address: String,
        @Embedded
        var payType: PT,
        @Embedded
        var payMethod: PM,
        @Embedded
        var paySchedule: PaySchedule,
        @Id
        var id: String = UUID.randomUUID().toString().uppercase(),
        var lastPayDay: LocalDate? = null
) : ru.aasmc.payrolsystem.domain.common.Entity<Employee<PT, PM>, String> {
    override fun getId(): String {
        return id
    }

    fun updateLastPayDay(date: LocalDate) {
        lastPayDay = date
    }
}

fun Employee<*, *>.requestAffiliationMembership(affiliationId: Long, affiliationService: AffiliationService) {
    affiliationService.addMember(id, affiliationId)
}

fun Employee<*, *>.leaveAffiliation(affiliationId: Long, affiliationService: AffiliationService) {
    affiliationService.removeMember(id, affiliationId)
}

fun Employee<HourlyType, *>.submitTimeCard(timeCard: TimeCard, accountingService: AccountingService) {
    accountingService.acceptTimeCard(timeCard)
}

fun Employee<HourlyType, *>.timeCardOf(date: LocalDate, hours: Int): TimeCard =
        TimeCard(date = date, hours = hours, employeeId = this.id)

fun Employee<CommissionedType, *>.salesReceiptOf(date: LocalDate, amount: BigDecimal): SalesReceipt =
        SalesReceipt(date = date, amount = amount, employeeId = this.id)

fun Employee<CommissionedType, *>.submitSalesReceipt(salesReceipt: SalesReceipt, accountingService: AccountingService) {
    accountingService.acceptSalesReceipt(salesReceipt)
}