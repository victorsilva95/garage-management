package com.estapar.garage.garage_management.aplication.web.controller.request

import jakarta.validation.constraints.NotNull

data class PostSpotStatusRequest(
    @NotNull val lat: Double,
    @NotNull val lng: Double,
)