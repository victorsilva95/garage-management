package com.estapar.garage.garage_management.domain.entity

import java.time.LocalDateTime

data class ParkingSpot(
    val id: Long? = null,
    val garageSector: GarageSector? = null,
    val lat: Double,
    val lng: Double,
    val occupied: Boolean? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun isOccupied() = occupied == true

}