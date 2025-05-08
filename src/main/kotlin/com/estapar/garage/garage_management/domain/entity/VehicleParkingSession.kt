package com.estapar.garage.garage_management.domain.entity

import com.estapar.garage.garage_management.common.Status
import java.math.BigDecimal
import java.time.LocalDateTime

data class VehicleParkingSession(
    val id: Long? = null,
    val parkingSpot: ParkingSpot? = null,
    val licensePlate: String,
    val entryTime: LocalDateTime? = null,
    val parkedTime: LocalDateTime? = null,
    val exitTime: LocalDateTime? = null,
    val status: Status,
    val totalPrice: BigDecimal? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)