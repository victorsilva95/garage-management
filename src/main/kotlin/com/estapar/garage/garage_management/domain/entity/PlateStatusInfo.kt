package com.estapar.garage.garage_management.domain.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class PlateStatusInfo(
    val licensePlate: String,
    val priceUntilNow: BigDecimal = BigDecimal.ZERO.setScale(2),
    val entryTime: LocalDateTime? = null,
    val timeParked: LocalDateTime? = null,
    val lat: Double? = null,
    val lng: Double? = null
)