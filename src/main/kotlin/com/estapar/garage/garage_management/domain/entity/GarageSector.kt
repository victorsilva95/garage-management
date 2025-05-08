package com.estapar.garage.garage_management.domain.entity

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

data class GarageSector(
    val id: Long? = null,
    val sectorCode: String,
    val basePrice: BigDecimal,
    val maxCapacity: Int,
    val openHour: LocalTime,
    val closeHour: LocalTime,
    val durationLimitMinutes: Int,
    val availableSpots: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun decrementAvailableSpots() = this.copy(availableSpots = availableSpots - 1)
    fun incrementAvailableSpots() = this.copy(availableSpots = availableSpots + 1)
}