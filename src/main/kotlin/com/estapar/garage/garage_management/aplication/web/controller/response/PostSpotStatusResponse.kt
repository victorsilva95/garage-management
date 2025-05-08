package com.estapar.garage.garage_management.aplication.web.controller.response

import com.estapar.garage.garage_management.domain.entity.SpotStatusInfo
import java.math.BigDecimal
import java.time.LocalDateTime

data class PostSpotStatusResponse(
    val ocupied: Boolean,
    val licensePlate: String?,
    val priceUntilNow: BigDecimal,
    val entryTime: LocalDateTime?,
    val timeParked: LocalDateTime?
) {
    companion object {
        fun fromDomain(spotStatusInfo: SpotStatusInfo) =
            with(spotStatusInfo) {
                PostSpotStatusResponse(
                    ocupied = ocupied,
                    licensePlate = licensePlate,
                    priceUntilNow = priceUntilNow,
                    entryTime = entryTime,
                    timeParked = timeParked
                )
            }
    }
}