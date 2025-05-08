package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.InitialSetupSpotInfo
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.entity.SetupGarageInitialInfo
import com.estapar.garage.garage_management.domain.service.GarageSectorService
import com.estapar.garage.garage_management.domain.service.ParkingSpotService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import java.math.BigDecimal
import java.time.LocalTime

class SetupGarageServiceImplTest {

    private val parkingSpotService: ParkingSpotService = mockk()
    private val garageSectorService: GarageSectorService = mockk()
    private val setupGarageService = SetupGarageServiceImpl(parkingSpotService, garageSectorService)

    private val baseGarageSector = GarageSector(
        id = 1,
        sectorCode = "A",
        basePrice = BigDecimal("40.50"),
        maxCapacity = 10,
        openHour = LocalTime.of(0, 0),
        closeHour = LocalTime.of(23, 59),
        durationLimitMinutes = 1440,
        availableSpots = 5
    )

    private val baseParkingSpot = ParkingSpot(
        id = 1,
        lat = 10.0,
        lng = 20.0,
        occupied = false,
        garageSector = baseGarageSector
    )

    private val baseInitialSetupSpotInfo = InitialSetupSpotInfo(
        lat = 10.0,
        lng = 20.0,
        sector = "A"
    )

    private val baseSetupGarageInitialInfo = SetupGarageInitialInfo(
        garageSector = listOf(baseGarageSector),
        initialSetupSpotInfo = listOf(baseInitialSetupSpotInfo)
    )

    @Test
    fun `createInitialSetupGarage should save garage sectors and parking spots`() {
        every { garageSectorService.findBySectorCode("A") } returns null andThen baseGarageSector
        every { garageSectorService.save(any()) } returns baseGarageSector
        every { parkingSpotService.findFullByLatAndLng(10.0, 20.0) } returns null
        every { parkingSpotService.save(any()) } returns baseParkingSpot

        setupGarageService.createInitialSetupGarage(baseSetupGarageInitialInfo)

        verify(exactly = 1) { garageSectorService.save(any()) }
        verify(exactly = 1) { parkingSpotService.save(any()) }
    }

    @Test
    fun `createInitialSetupGarage should update existing garage sector`() {
        every { garageSectorService.findBySectorCode("A") } returns baseGarageSector
        every { garageSectorService.save(any()) } returns baseGarageSector
        every { parkingSpotService.findFullByLatAndLng(10.0, 20.0) } returns null
        every { parkingSpotService.save(any()) } returns baseParkingSpot

        setupGarageService.createInitialSetupGarage(baseSetupGarageInitialInfo)

        verify(exactly = 1) { garageSectorService.save(match { it.id == 1L }) }
        verify(exactly = 1) { parkingSpotService.save(any()) }
    }

    @Test
    fun `createInitialSetupGarage should throw exception when sector is not found`() {
        every { garageSectorService.findBySectorCode("A") } returns null
        every { garageSectorService.save(any()) } returns baseGarageSector
        every { parkingSpotService.findFullByLatAndLng(10.0, 20.0) } returns null
        every { garageSectorService.findBySectorCode("B") } returns null

        val exception = assertThrows<HttpClientErrorException> {
            setupGarageService.createInitialSetupGarage(
                baseSetupGarageInitialInfo.copy(
                    initialSetupSpotInfo = listOf(
                        baseInitialSetupSpotInfo.copy(sector = "B")
                    )
                )
            )
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("422 Sector with status B not found", exception.message)
        verify(exactly = 0) { parkingSpotService.save(any()) }
    }
}