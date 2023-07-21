package ru.aasmc.payrolsystem.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.aasmc.payrolsystem.dto.ServiceChargeRequest
import ru.aasmc.payrolsystem.dto.UnionAffiliationDto
import ru.aasmc.payrolsystem.service.AffiliationService

private val log = LoggerFactory.getLogger(AffiliationController::class.java)

@RestController
@RequestMapping("/affiliations")
class AffiliationController(
        private val affiliationService: AffiliationService
) {

    @GetMapping
    fun getAllUnions(): List<UnionAffiliationDto> {
        log.info("Received request to GET all UnionAffiliations")
        return affiliationService.getAllUnionAffiliations()
    }

    @PostMapping
    fun createUnion(@RequestBody dto: UnionAffiliationDto): UnionAffiliationDto {
        log.info("Received POST request to create UnionAffiliation {}", dto)
        return affiliationService.createUnionAffiliation(dto)
    }

    @PostMapping("/charge")
    fun createServiceCharge(@RequestBody dto: ServiceChargeRequest) {
        log.info("Received POST request to create service charge {}", dto)
        affiliationService.createServiceCharge(dto)
    }

}