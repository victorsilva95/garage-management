package com.estapar.garage.garage_management.domain.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class RevenueInfo(
    val amount: BigDecimal = BigDecimal.ZERO.setScale(2),
    val currency: String = "BRL",
    val timestamp: LocalDateTime = LocalDateTime.now()
)