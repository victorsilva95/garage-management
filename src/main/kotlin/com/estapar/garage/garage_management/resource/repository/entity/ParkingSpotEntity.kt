package com.estapar.garage.garage_management.resource.repository.entity

import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.Temporal
import java.time.LocalDateTime

@Entity(name = "ParkingSpot")
@Table(name = "parking_spot")
data class ParkingSpotEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    val garageSectorEntity: GarageSectorEntity,

    @Column(nullable = false)
    val lat: Double,

    @Column(nullable = false)
    val lng: Double,

    @Column(nullable = false)
    val occupied: Boolean,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    val updatedAt: LocalDateTime,
) {
    fun toDomain(): ParkingSpot =
        ParkingSpot(
            id = this.id,
            garageSector = garageSectorEntity.toDomain(),
            lat = lat,
            lng = lng,
            occupied = occupied,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun fromDomain(parkingSpot: ParkingSpot): ParkingSpotEntity =
            with(parkingSpot) {
                ParkingSpotEntity(
                    id = id,
                    garageSectorEntity = GarageSectorEntity.fromDomain(requireNotNull(garageSector)),
                    lat = lat,
                    lng = lng,
                    occupied = requireNotNull(occupied),
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
    }
}