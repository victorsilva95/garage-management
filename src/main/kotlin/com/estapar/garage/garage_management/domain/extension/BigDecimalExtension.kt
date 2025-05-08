package com.estapar.garage.garage_management.domain.extension

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.calculateDynamicPrice(maxCapacity: Int, availableSpots: Int): BigDecimal {
    val occupiedSpots = maxCapacity - availableSpots
    val occupancy = occupiedSpots.toBigDecimal().divide(
        maxCapacity.toBigDecimal(), 2, RoundingMode.HALF_UP
    )
    return when {
        occupancy < BigDecimal("0.25") -> this * BigDecimal("0.90")
        occupancy < BigDecimal("0.50") -> this
        occupancy < BigDecimal("0.75") -> this * BigDecimal("1.10")
        else -> this * BigDecimal("1.25")
    }.setScale(2, RoundingMode.HALF_UP)
}