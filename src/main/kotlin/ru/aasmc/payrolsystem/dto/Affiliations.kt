package ru.aasmc.payrolsystem.dto

import java.math.BigDecimal

data class UnionAffiliationDto(
        val id: Long? = null,
        val dues: BigDecimal,
        val memberIds: List<Long> = emptyList()
)