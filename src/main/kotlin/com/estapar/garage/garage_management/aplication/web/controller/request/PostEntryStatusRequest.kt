package com.estapar.garage.garage_management.aplication.web.controller.request

import com.estapar.garage.garage_management.common.Status
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class PostEntryStatusRequest(
    @NotEmpty val licensePlate: String,
    @NotNull val entryTime: LocalDateTime,
    @NotNull val eventType: Status
) {
    fun toDomain(): VehicleParkingSession =
        VehicleParkingSession(
            licensePlate = licensePlate,
            entryTime = entryTime,
            status = eventType
        )
}