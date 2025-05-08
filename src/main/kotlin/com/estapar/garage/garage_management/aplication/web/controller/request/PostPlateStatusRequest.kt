package com.estapar.garage.garage_management.aplication.web.controller.request

import jakarta.validation.constraints.NotEmpty

data class PostPlateStatusRequest(
    @NotEmpty val licensePlate: String
)