package com.estapar.garage.garage_management.resource.repository.entity

import com.estapar.garage.garage_management.common.Status
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.Temporal
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "VehicleParkingSession")
@Table(name = "vehicle_parking_session")
data class VehicleParkingSessionEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    val parkingSpotEntity: ParkingSpotEntity? = null,

    @Column(nullable = false)
    val licensePlate: String,

    @Column(nullable = false)
    val entryTime: LocalDateTime,

    @Column
    val parkedTime: LocalDateTime? = null,

    @Column
    val exitTime: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status,

    @Column
    val totalPrice: BigDecimal? = null,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    val updatedAt: LocalDateTime,
) {
    fun toDomain(): VehicleParkingSession =
        VehicleParkingSession(
            id = this.id,
            parkingSpot = parkingSpotEntity?.toDomain(),
            licensePlate = licensePlate,
            entryTime = entryTime,
            parkedTime = parkedTime,
            exitTime = exitTime,
            status = status,
            totalPrice = totalPrice,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun fromDomain(vehicleParkingSession: VehicleParkingSession): VehicleParkingSessionEntity =
            with(vehicleParkingSession) {
                VehicleParkingSessionEntity(
                    id = id,
                    parkingSpotEntity = parkingSpot?.let { ParkingSpotEntity.fromDomain(it) },
                    licensePlate = licensePlate,
                    entryTime = requireNotNull(entryTime),
                    parkedTime = parkedTime,
                    exitTime = exitTime,
                    status = status,
                    totalPrice = totalPrice,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}