package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.common.Status
import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import com.estapar.garage.garage_management.domain.repository.VehicleParkingSessionRepository
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
import java.time.LocalDateTime
import java.time.LocalTime

class VehicleParkingSessionServiceImplTest {

    private val parkingSpotService: ParkingSpotService = mockk()
    private val garageSectorService: GarageSectorService = mockk()
    private val vehicleParkingSessionRepository: VehicleParkingSessionRepository = mockk()
    private val vehicleParkingSessionService = VehicleParkingSessionServiceImpl(
        parkingSpotService, garageSectorService, vehicleParkingSessionRepository
    )

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

    private val baseVehicleParkingSession = VehicleParkingSession(
        id = 1,
        licensePlate = "ABC1234",
        status = Status.ENTRY,
        entryTime = LocalDateTime.now(),
        parkedTime = null,
        exitTime = null,
        totalPrice = null,
        parkingSpot = null
    )

    @Test
    fun `saveEntryStatus should throw exception when no spots are available`() {
        every { garageSectorService.checkAvailableSpots() } returns false

        val exception = assertThrows<HttpClientErrorException> {
            vehicleParkingSessionService.saveEntryStatus(baseVehicleParkingSession)
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("422 There are no parking spaces available", exception.message)
        verify(exactly = 1) { garageSectorService.checkAvailableSpots() }
    }

    @Test
    fun `saveEntryStatus should throw exception when vehicle is already parked`() {
        every { garageSectorService.checkAvailableSpots() } returns true
        every { vehicleParkingSessionRepository.findByLicensePlateWithStatusEntryOrParked(baseVehicleParkingSession) } returns baseVehicleParkingSession

        val exception = assertThrows<HttpClientErrorException> {
            vehicleParkingSessionService.saveEntryStatus(baseVehicleParkingSession)
        }

        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
        assertEquals("409 There is already a vehicle in the garage or parked with this plate", exception.message)
        verify(exactly = 1) { vehicleParkingSessionRepository.findByLicensePlateWithStatusEntryOrParked(baseVehicleParkingSession) }
    }

    @Test
    fun `saveEntryStatus should save vehicle parking session`() {
        every { garageSectorService.checkAvailableSpots() } returns true
        every { vehicleParkingSessionRepository.findByLicensePlateWithStatusEntryOrParked(baseVehicleParkingSession) } returns null
        every { vehicleParkingSessionRepository.save(baseVehicleParkingSession) } returns baseVehicleParkingSession

        vehicleParkingSessionService.saveEntryStatus(baseVehicleParkingSession)

        verify(exactly = 1) { vehicleParkingSessionRepository.save(baseVehicleParkingSession) }
    }

    @Test
    fun `saveParkedStatus should throw exception when vehicle is not in entry status`() {
        every { vehicleParkingSessionRepository.findByStatusEntry(baseVehicleParkingSession) } returns null

        val exception = assertThrows<HttpClientErrorException> {
            vehicleParkingSessionService.saveParkedStatus(baseVehicleParkingSession, 10.0, 20.0)
        }

        assertEquals(HttpStatus.CONFLICT, exception.statusCode)
        assertEquals("409 There is already a vehicle in the garage or parked with this plate", exception.message)
        verify(exactly = 1) { vehicleParkingSessionRepository.findByStatusEntry(baseVehicleParkingSession) }
    }

    @Test
    fun `saveParkedStatus should save parked status`() {
        val updatedSession = baseVehicleParkingSession.copy(status = Status.PARKED, parkedTime = LocalDateTime.now())
        every { vehicleParkingSessionRepository.findByStatusEntry(baseVehicleParkingSession) } returns baseVehicleParkingSession
        every { parkingSpotService.markSpotAsOccupied(10.0, 20.0) } returns baseParkingSpot
        every { vehicleParkingSessionRepository.save(any()) } returns updatedSession

        vehicleParkingSessionService.saveParkedStatus(baseVehicleParkingSession, 10.0, 20.0)

        verify(exactly = 1) { parkingSpotService.markSpotAsOccupied(10.0, 20.0) }
        verify(exactly = 1) { vehicleParkingSessionRepository.save(any()) }
    }

    @Test
    fun `saveExitStatus should throw exception when vehicle is not in entry or parked status`() {
        every { vehicleParkingSessionRepository.findFullByStatusEntryOrParked(baseVehicleParkingSession.licensePlate) } returns null

        val exception = assertThrows<HttpClientErrorException> {
            vehicleParkingSessionService.saveExitStatus(baseVehicleParkingSession)
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("422 The vehicle must be in entry or parked status", exception.message)
        verify(exactly = 1) { vehicleParkingSessionRepository.findFullByStatusEntryOrParked(baseVehicleParkingSession.licensePlate) }
    }

    @Test
    fun `saveExitStatus should save exit status`() {
        val sessionWithSpot = baseVehicleParkingSession.copy(parkingSpot = baseParkingSpot)
        val updatedSession = sessionWithSpot.copy(
            status = Status.EXITED,
            exitTime = LocalDateTime.now(),
            totalPrice = BigDecimal("50.00")
        )
        every { vehicleParkingSessionRepository.findFullByStatusEntryOrParked(sessionWithSpot.licensePlate) } returns sessionWithSpot
        every { parkingSpotService.markSpotAsUnoccupied(any(), any()) } returns baseGarageSector
        every { vehicleParkingSessionRepository.save(any()) } returns updatedSession

        vehicleParkingSessionService.saveExitStatus(sessionWithSpot)

        verify(exactly = 1) { parkingSpotService.markSpotAsUnoccupied(any(), any()) }
        verify(exactly = 1) { vehicleParkingSessionRepository.save(any()) }
    }
}