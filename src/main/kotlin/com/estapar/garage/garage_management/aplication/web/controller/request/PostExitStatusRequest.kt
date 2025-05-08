package com.estapar.garage.garage_management.aplication.web.controller.request

import com.estapar.garage.garage_management.common.Status
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class PostExitStatusRequest(
    @NotEmpty val licensePlate: String,
    @NotNull val exitTime: LocalDateTime,
    @NotNull val eventType: Status
) {
    fun toDomain(): VehicleParkingSession =
        VehicleParkingSession(
            licensePlate = licensePlate,
            exitTime = exitTime,
            status = eventType
        )
}