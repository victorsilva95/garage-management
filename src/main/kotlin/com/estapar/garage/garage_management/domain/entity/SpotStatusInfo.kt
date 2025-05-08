package com.estapar.garage.garage_management.domain.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class SpotStatusInfo(
    val ocupied: Boolean = false,
    val licensePlate: String? = null,
    val priceUntilNow: BigDecimal = BigDecimal.ZERO.setScale(2),
    val entryTime: LocalDateTime? = null,
    val timeParked: LocalDateTime? = null
)