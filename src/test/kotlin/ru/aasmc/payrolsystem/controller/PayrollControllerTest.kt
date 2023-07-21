package ru.aasmc.payrolsystem.controller

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import ru.aasmc.payrolsystem.BaseTest
import ru.aasmc.payrolsystem.dto.EmployeeResponse
import ru.aasmc.payrolsystem.dto.PayCheckResponse
import ru.aasmc.payrolsystem.dto.UnionAffiliationDto
import ru.aasmc.payrolsystem.testutil.*
import java.time.LocalDate
import java.time.Month

class PayrollControllerTest : BaseTest() {

    @Test
    fun whenCommissionedEmployeeIsInUnionWithSomeServiceChargesPastDue_onlyDueDeductionsAreInPayCheck() {
        val employee = webTestClient.post().uri("/employees/commissioned")
                .bodyValue(getCommissionedEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!, LocalDate.of(1023, Month.JUNE, 1)))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/salesreceipt")
                .bodyValue(getSalesReceiptDto(employee.id))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.get().uri("/payroll?date=2023-07-21")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("7.5000")
                    assertThat(first.netPay).isEqualTo("4.5000") // two weeks == two Fridays, union charges dues on each friday
                    assertThat(first.deductions).isEqualTo("3.00")
                }
    }

    @Test
    fun whenCommissionedEmployeeIsInUnionWithSeveralServiceCharges_allDeductionsAreInPayCheck() {
        val employee = webTestClient.post().uri("/employees/commissioned")
                .bodyValue(getCommissionedEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/salesreceipt")
                .bodyValue(getSalesReceiptDto(employee.id))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.get().uri("/payroll?date=2023-07-21")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("7.5000")
                    assertThat(first.netPay).isEqualTo("3.5000") // two weeks == two Fridays, union charges dues on each friday
                    assertThat(first.deductions).isEqualTo("4.00")
                }
    }

    @Test
    fun whenCommissionedEmployeeIsInUnionWithServiceCharge_deductionsAreInPayCheck() {
        val employee = webTestClient.post().uri("/employees/commissioned")
                .bodyValue(getCommissionedEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/salesreceipt")
                .bodyValue(getSalesReceiptDto(employee.id))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.get().uri("/payroll?date=2023-07-21")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("7.5000")
                    assertThat(first.netPay).isEqualTo("4.5000") // two weeks == two Fridays, union charges dues on each friday
                    assertThat(first.deductions).isEqualTo("3.00")
                }
    }

    @Test
    fun whenHourlyEmployeeHasSeveralDueTimeCards_theyAreAllIncludedInPayCheck() {
        val employee = webTestClient.post().uri("/employees/hourly")
                .bodyValue(getHourlyEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/timecard")
                .bodyValue(getTimeCardDto(employee.id, 8))
                .exchange()
                .expectStatus().is2xxSuccessful // Total Salary = 8

        webTestClient.post().uri("/employees/timecard")
                .bodyValue(getTimeCardDto(employee.id, 16, LocalDate.of(2023, Month.JULY, 20)))
                .exchange()
                .expectStatus().is2xxSuccessful // Total Salary = 8 + 8 + 8 * 1.5 = 28

        webTestClient.get().uri("/payroll?date=2023-07-21")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("28.000")
                    assertThat(first.netPay).isEqualTo("26.000")
                    assertThat(first.deductions).isEqualTo("2.00")
                }
    }

    @Test
    fun whenHourlyEmployeeHasPastDueTimeCards_theyAreNotIncludedInPayCheck() {
        val employee = webTestClient.post().uri("/employees/hourly")
                .bodyValue(getHourlyEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/timecard")
                .bodyValue(getTimeCardDto(employee.id, 8))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/timecard")
                .bodyValue(getTimeCardDto(employee.id, 8, LocalDate.of(2023, Month.JUNE, 1)))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.get().uri("/payroll?date=2023-07-21")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("8.000")
                    assertThat(first.netPay).isEqualTo("6.000")
                    assertThat(first.deductions).isEqualTo("2.00")
                }
    }

    @Test
    fun whenHourlyEmployeeIsInUnionWithServiceCharge_deductionsAreInPayCheck() {
        val employee = webTestClient.post().uri("/employees/hourly")
                .bodyValue(getHourlyEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/employees/timecard")
                .bodyValue(getTimeCardDto(employee.id, 8))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.get().uri("/payroll?date=2023-07-21")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("8.000")
                    assertThat(first.netPay).isEqualTo("6.000")
                    assertThat(first.deductions).isEqualTo("2.00")
                }
    }

    @Test
    fun whenSalaryEmployeeIsInUnionWithServiceCharge_deductionsAreInPaycheck() {
        val employee = webTestClient.post().uri("/employees/salary")
                .bodyValue(getSalaryEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val union = webTestClient.post().uri("/affiliations")
                .bodyValue(getUnionAffiliationDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(UnionAffiliationDto::class.java)
                .returnResult().responseBody!!

        webTestClient.patch().uri("/employees/affiliation/${employee.id}/${union.id}")
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.post().uri("/affiliations/charge")
                .bodyValue(getServiceChargeDto(employee.id, union.id!!))
                .exchange()
                .expectStatus().is2xxSuccessful

        webTestClient.get().uri("/payroll?date=2023-07-31")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(1)
                    val first = employeeIdToCheck[employee.id]!!
                    assertThat(first.grossPay).isEqualTo("10.00")
                    assertThat(first.netPay).isEqualTo("5.00") // 4 Fridays + 1 ServiceCharge
                    assertThat(first.deductions).isEqualTo("5.00")
                }
    }


    @Test
    fun whenPayOnLastDayOfMonthNotFriday_salaryReceived_hourlyAndCommissionNot() {
        val one = webTestClient.post().uri("/employees/salary")
                .bodyValue(getSalaryEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val two = webTestClient.post().uri("/employees/hourly")
                .bodyValue(getHourlyEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        val three = webTestClient.post().uri("/employees/commissioned")
                .bodyValue(getCommissionedEmployeeDto())
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(EmployeeResponse::class.java)
                .returnResult().responseBody!!

        webTestClient.get().uri("/payroll?date=2023-07-31")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    val employeeIdToCheck = response.paychecks.associateBy { it.employeeId }
                    assertThat(employeeIdToCheck.size).isEqualTo(3)
                    val first = employeeIdToCheck[one.id]!!
                    assertThat(first.grossPay).isEqualTo("10.00")
                    assertThat(first.netPay).isEqualTo("10.00")

                    val second = employeeIdToCheck[two.id]!!
                    assertThat(second.grossPay).isEqualTo("0")
                    assertThat(second.netPay).isEqualTo("0")

                    val third = employeeIdToCheck[three.id]!!
                    assertThat(third.grossPay).isEqualTo("0")
                    assertThat(third.netPay).isEqualTo("0")
                }
    }

    @Test
    fun whenNoEmployees_emptyResponse() {
        webTestClient.get().uri("/payroll?date=2023-07-31")
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody(PayCheckResponse::class.java).value { response ->
                    assertThat(response.paychecks).isEmpty()
                }

    }


}