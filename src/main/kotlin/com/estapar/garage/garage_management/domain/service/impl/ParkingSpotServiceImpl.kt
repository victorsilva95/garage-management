package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.repository.ParkingSpotRepository
import com.estapar.garage.garage_management.domain.service.GarageSectorService
import com.estapar.garage.garage_management.domain.service.ParkingSpotService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
class ParkingSpotServiceImpl(
    val parkingSpotRepository: ParkingSpotRepository,
    val garageSectorService: GarageSectorService
) : ParkingSpotService {

    override fun markSpotAsOccupied(
        lat: Double,
        lng: Double
    ): ParkingSpot {
        val parkingSpot = this.findFullByLatAndLng(lat, lng) ?: throw HttpClientErrorException(
            HttpStatus.UNPROCESSABLE_ENTITY, "Spot not found"
        )
        if (parkingSpot.isOccupied()) {
            throw HttpClientErrorException(
                HttpStatus.UNPROCESSABLE_ENTITY, "Spot is already occupied"
            )
        }
        val savedParkingSpot = this.save(
            parkingSpot.copy(
                occupied = true
            )
        )
        garageSectorService.decrementAvailableSectors(parkingSpot.garageSector)
        return savedParkingSpot
    }

    override fun markSpotAsUnoccupied(
        lat: Double,
        lng: Double
    ): GarageSector {
        val parkingSpot = this.findFullByLatAndLng(lat, lng) ?: throw HttpClientErrorException(
            HttpStatus.UNPROCESSABLE_ENTITY, "Spot not found"
        )
        this.save(
            parkingSpot.copy(
                occupied = false
            )
        )
        val garageSector = garageSectorService.incrementAvailableSectors(parkingSpot.garageSector)
        return garageSector
    }

    override fun findFullByLatAndLng(lat: Double, lng: Double): ParkingSpot? =
        parkingSpotRepository.findFullByLatAndLng(lat, lng)

    override fun save(parkingSpot: ParkingSpot): ParkingSpot =
        parkingSpotRepository.save(parkingSpot)
}