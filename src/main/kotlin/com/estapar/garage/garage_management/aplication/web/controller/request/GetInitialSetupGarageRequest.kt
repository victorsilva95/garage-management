package com.estapar.garage.garage_management.aplication.web.controller.request

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.InitialSetupSpotInfo
import com.estapar.garage.garage_management.domain.entity.SetupGarageInitialInfo
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalTime

data class GetInitialSetupGarageRequest(
    @NotNull val garage: List<GarageRequest>,
    @NotNull val spots: List<SpotRequest>
) {
    fun toDomain() = SetupGarageInitialInfo(
        garageSector = garage.map { it.toDomain() },
        initialSetupSpotInfo = spots.map { it.toDomain() }
    )
}

data class GarageRequest(
    @NotEmpty val sector: String,
    @NotNull val basePrice: BigDecimal,
    @NotNull val maxCapacity: Int,
    @NotNull val openHour: LocalTime,
    @NotNull val closeHour: LocalTime,
    @NotNull val durationLimitMinutes: Int
) {
    fun toDomain() = GarageSector(
        sectorCode = sector,
        basePrice = basePrice,
        maxCapacity = maxCapacity,
        openHour = openHour,
        closeHour = closeHour,
        durationLimitMinutes = durationLimitMinutes,
        availableSpots = maxCapacity
    )
}

data class SpotRequest(
    @NotNull val id: Int,
    @NotEmpty val sector: String,
    @NotNull val lat: Double,
    @NotNull val lng: Double
) {
    fun toDomain() = InitialSetupSpotInfo(
        lat = lat,
        lng = lng,
        sector = sector
    )
}