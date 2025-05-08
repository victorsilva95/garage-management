package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.InitialSetupSpotInfo
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.entity.SetupGarageInitialInfo
import com.estapar.garage.garage_management.domain.service.GarageSectorService
import com.estapar.garage.garage_management.domain.service.ParkingSpotService
import com.estapar.garage.garage_management.domain.service.SetupGarageService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
class SetupGarageServiceImpl(
    val parkingSpotService: ParkingSpotService,
    val garageSectorService: GarageSectorService,
) : SetupGarageService {

    @Transactional
    override fun createInitialSetupGarage(setupGarageInitialInfo: SetupGarageInitialInfo) {
        initialSetupSector(setupGarageInitialInfo.garageSector)
        initialSetupSpot(setupGarageInitialInfo.initialSetupSpotInfo)
    }

    private fun initialSetupSector(garageSectorList: List<GarageSector>) {
        garageSectorList.forEach { garageSector ->
            val garageSectorSave = garageSectorService.findBySectorCode(garageSector.sectorCode)
                ?.copy(
                    sectorCode = garageSector.sectorCode,
                    basePrice = garageSector.basePrice,
                    maxCapacity = garageSector.maxCapacity,
                    openHour = garageSector.openHour,
                    closeHour = garageSector.closeHour,
                    durationLimitMinutes = garageSector.durationLimitMinutes,
                    availableSpots = garageSector.availableSpots
                ) ?: GarageSector(
                sectorCode = garageSector.sectorCode,
                basePrice = garageSector.basePrice,
                maxCapacity = garageSector.maxCapacity,
                openHour = garageSector.openHour,
                closeHour = garageSector.closeHour,
                durationLimitMinutes = garageSector.durationLimitMinutes,
                availableSpots = garageSector.availableSpots
            )
            garageSectorService.save(garageSectorSave)
        }
    }

    private fun initialSetupSpot(initialSetupSpotInfoList: List<InitialSetupSpotInfo>) {
        initialSetupSpotInfoList.forEach { initialSetupSpotInfo ->
            val garageSector = garageSectorService.findBySectorCode(initialSetupSpotInfo.sector)
                ?: throw HttpClientErrorException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Sector with status ${initialSetupSpotInfo.sector} not found"
                )
            val parkingSpotSaved =
                parkingSpotService.findFullByLatAndLng(
                    initialSetupSpotInfo.lat,
                    initialSetupSpotInfo.lng
                )?.copy(
                    lat = initialSetupSpotInfo.lat,
                    lng = initialSetupSpotInfo.lng,
                    garageSector = garageSector,
                    occupied = false
                ) ?: ParkingSpot(
                    lat = initialSetupSpotInfo.lat,
                    lng = initialSetupSpotInfo.lng,
                    garageSector = garageSector,
                    occupied = false
                )
            parkingSpotService.save(parkingSpotSaved)
        }
    }

}