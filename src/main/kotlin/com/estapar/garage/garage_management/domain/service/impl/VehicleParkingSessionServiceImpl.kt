package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.common.Status.EXITED
import com.estapar.garage.garage_management.common.Status.PARKED
import com.estapar.garage.garage_management.domain.entity.PlateStatusInfo
import com.estapar.garage.garage_management.domain.entity.RevenueInfo
import com.estapar.garage.garage_management.domain.entity.SpotStatusInfo
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import com.estapar.garage.garage_management.domain.extension.calculateDynamicPrice
import com.estapar.garage.garage_management.domain.repository.VehicleParkingSessionRepository
import com.estapar.garage.garage_management.domain.service.GarageSectorService
import com.estapar.garage.garage_management.domain.service.ParkingSpotService
import com.estapar.garage.garage_management.domain.service.VehicleParkingSessionService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class VehicleParkingSessionServiceImpl(
    val parkingSpotService: ParkingSpotService,
    val garageSectorService: GarageSectorService,
    val vehicleParkingSessionRepository: VehicleParkingSessionRepository
) : VehicleParkingSessionService {
    override fun saveEntryStatus(vehicleParkingSession: VehicleParkingSession) {
        if (!garageSectorService.checkAvailableSpots()) {
            throw HttpClientErrorException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "There are no parking spaces available"
            )
        }
        if (vehicleParkingSessionRepository.findByLicensePlateWithStatusEntryOrParked(vehicleParkingSession) != null) {
            throw HttpClientErrorException(
                CONFLICT, "There is already a vehicle in the garage or parked with this plate"
            )
        }
        vehicleParkingSessionRepository.save(vehicleParkingSession)
    }

    @Transactional
    override fun saveParkedStatus(vehicleParkingSession: VehicleParkingSession, lat: Double, lng: Double) {
        val vehicleParkingSessionDatabase =
            vehicleParkingSessionRepository.findByStatusEntry(vehicleParkingSession)
                ?: throw HttpClientErrorException(
                    CONFLICT, "There is already a vehicle in the garage or parked with this plate"
                )
        val parkingSpot = parkingSpotService.markSpotAsOccupied(lat, lng)
        vehicleParkingSessionRepository.save(
            vehicleParkingSessionDatabase.copy(
                parkingSpot = parkingSpot,
                status = PARKED,
                parkedTime = LocalDateTime.now()
            )
        )

    }

    @Transactional
    override fun saveExitStatus(vehicleParkingSession: VehicleParkingSession) {
        val vehicleParkingSessionDatabase =
            vehicleParkingSessionRepository.findFullByStatusEntryOrParked(vehicleParkingSession.licensePlate)
                ?: throw HttpClientErrorException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "The vehicle must be in entry or parked status"
                )
        requireNotNull(vehicleParkingSessionDatabase.parkingSpot)
        val garageSector = parkingSpotService.markSpotAsUnoccupied(
            vehicleParkingSessionDatabase.parkingSpot.lat,
            vehicleParkingSessionDatabase.parkingSpot.lng
        )
        vehicleParkingSessionRepository.save(
            vehicleParkingSessionDatabase.copy(
                status = EXITED,
                exitTime = vehicleParkingSession.exitTime,
                totalPrice = garageSector.basePrice.calculateDynamicPrice(
                    garageSector.maxCapacity,
                    garageSector.availableSpots
                )
            )
        )
    }

    override fun getPlaceStatus(licensePlate: String) =
        vehicleParkingSessionRepository.findFullByStatusEntryOrParked(licensePlate)
            ?.let {
                val parkingSpot = requireNotNull(it.parkingSpot)
                val garageSector = requireNotNull(parkingSpot.garageSector)
                PlateStatusInfo(
                    licensePlate = licensePlate,
                    priceUntilNow = garageSector.basePrice.calculateDynamicPrice(
                        garageSector.maxCapacity,
                        garageSector.availableSpots
                    ),
                    entryTime = it.entryTime,
                    timeParked = it.parkedTime,
                    lat = parkingSpot.lat,
                    lng = parkingSpot.lng
                )
            } ?: PlateStatusInfo(licensePlate = licensePlate)

    override fun getSpotStatus(
        lat: Double,
        lng: Double
    ): SpotStatusInfo =
        vehicleParkingSessionRepository.findFullByLatLngAndStatusParked(lat, lng)
            ?.let {
                val parkingSpot = requireNotNull(it.parkingSpot)
                val garageSector = requireNotNull(parkingSpot.garageSector)
                SpotStatusInfo(
                    ocupied = true,
                    licensePlate = it.licensePlate,
                    priceUntilNow = garageSector.basePrice.calculateDynamicPrice(
                        garageSector.maxCapacity,
                        garageSector.availableSpots
                    ),
                    entryTime = it.entryTime,
                    timeParked = it.parkedTime,
                )
            } ?: SpotStatusInfo()

    override fun getRevenue(
        date: LocalDate,
        sector: String
    ): RevenueInfo =
        vehicleParkingSessionRepository.sumByStatusExitedAndSectorAndDate(date, sector)
            ?.let { RevenueInfo(it.setScale(2)) } ?: RevenueInfo()
}