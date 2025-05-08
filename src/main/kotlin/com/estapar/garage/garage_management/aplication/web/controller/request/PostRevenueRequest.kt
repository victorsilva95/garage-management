package com.estapar.garage.garage_management.aplication.web.controller.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class PostRevenueRequest(
    @NotNull val date: LocalDate,
    @NotEmpty val sector: String,
)