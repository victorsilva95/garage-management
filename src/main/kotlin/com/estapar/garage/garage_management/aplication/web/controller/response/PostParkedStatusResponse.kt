package com.estapar.garage.garage_management.aplication.web.controller.response

import com.estapar.garage.garage_management.domain.entity.PlateStatusInfo
import java.math.BigDecimal
import java.time.LocalDateTime

data class PostParkedStatusResponse(
    val licensePlate: String,
    val priceUntilNow: BigDecimal,
    val entryTime: LocalDateTime?,
    val timeParked: LocalDateTime?,
    val lat: Double?,
    val lng: Double?
) {
    companion object {
        fun fromDomain(plateStatusInfo: PlateStatusInfo) =
            with(plateStatusInfo) {
                PostParkedStatusResponse(
                    licensePlate = licensePlate,
                    priceUntilNow = priceUntilNow,
                    entryTime = entryTime,
                    timeParked = timeParked,
                    lat = lat,
                    lng = lng
                )
            }
    }
}