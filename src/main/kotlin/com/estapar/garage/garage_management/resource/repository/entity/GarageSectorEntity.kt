package com.estapar.garage.garage_management.resource.repository.entity

import com.estapar.garage.garage_management.domain.entity.GarageSector
import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.Temporal
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(name = "GarageSector")
@Table(name = "garage_sector")
data class GarageSectorEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val sectorCode: String,

    @Column(nullable = false)
    val basePrice: BigDecimal,

    @Column(nullable = false)
    val maxCapacity: Int,

    @Column(nullable = false)
    val openHour: LocalTime,

    @Column(nullable = false)
    val closeHour: LocalTime,

    @Column(nullable = false)
    val durationLimitMinutes: Int,

    @Column(nullable = false)
    val availableSpots: Int,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    val updatedAt: LocalDateTime
) {
    fun toDomain(): GarageSector =
        GarageSector(
            id = id,
            sectorCode = sectorCode,
            basePrice = basePrice,
            maxCapacity = maxCapacity,
            openHour = openHour,
            closeHour = closeHour,
            durationLimitMinutes = durationLimitMinutes,
            availableSpots = availableSpots,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    companion object {
        fun fromDomain(garageSector: GarageSector) =
            with(garageSector) {
                GarageSectorEntity(
                    id = id,
                    sectorCode = sectorCode,
                    basePrice = basePrice,
                    maxCapacity = maxCapacity,
                    openHour = openHour,
                    closeHour = closeHour,
                    durationLimitMinutes = durationLimitMinutes,
                    availableSpots = availableSpots,
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
    }
}