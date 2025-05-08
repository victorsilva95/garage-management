package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.repository.ParkingSpotRepository
import com.estapar.garage.garage_management.resource.repository.entity.ParkingSpotEntity
import org.springframework.stereotype.Repository

@Repository
class ParkingSpotRepositoryImpl(val parkingSpotRepositoryJpa: ParkingSpotRepositoryJpa) : ParkingSpotRepository {

    override fun findFullByLatAndLng(lat: Double, lng: Double): ParkingSpot? =
        parkingSpotRepositoryJpa.findFullByLatAndLng(lat, lng)?.toDomain()

    override fun save(parkingSpot: ParkingSpot): ParkingSpot =
        parkingSpotRepositoryJpa.save(ParkingSpotEntity.fromDomain(parkingSpot))
            .toDomain()
}