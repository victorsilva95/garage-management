package com.estapar.garage.garage_management.aplication.web.controller.response

import com.estapar.garage.garage_management.domain.entity.RevenueInfo
import java.math.BigDecimal
import java.time.LocalDateTime

data class PostRevenueResponse(
    val amount: BigDecimal,
    val currency: String,
    val timestamp: LocalDateTime
) {
    companion object {
        fun fromDomain(revenueInfo: RevenueInfo) =
            with(revenueInfo) {
                PostRevenueResponse(
                    amount = amount,
                    currency = currency,
                    timestamp = timestamp
                )
            }
    }
}