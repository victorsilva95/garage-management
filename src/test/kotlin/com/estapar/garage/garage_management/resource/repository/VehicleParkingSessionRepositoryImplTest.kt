package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.common.Status.PARKED
import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import com.estapar.garage.garage_management.resource.repository.entity.GarageSectorEntity
import com.estapar.garage.garage_management.resource.repository.entity.ParkingSpotEntity
import com.estapar.garage.garage_management.resource.repository.entity.VehicleParkingSessionEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class VehicleParkingSessionRepositoryImplTest {

    private val vehicleParkingSessionRepositoryJpa: VehicleParkingSessionRepositoryJpa = mockk()
    private val vehicleParkingSessionRepository =
        VehicleParkingSessionRepositoryImpl(vehicleParkingSessionRepositoryJpa)

    private val baseVehicleParkingSession = VehicleParkingSession(
        id = 1,
        licensePlate = "ABC1234",
        status = PARKED,
        entryTime = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
        parkedTime = null,
        exitTime = null,
        totalPrice = null,
        parkingSpot = ParkingSpot(
            id = 1,
            lat = 10.0,
            lng = 20.0,
            occupied = false,
            garageSector = GarageSector(
                id = 1,
                sectorCode = "A",
                basePrice = BigDecimal("40.50"),
                maxCapacity = 10,
                openHour = LocalTime.of(0, 0),
                closeHour = LocalTime.of(23, 59),
                durationLimitMinutes = 1440,
                availableSpots = 5,
                createdAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
                updatedAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
            ),
            createdAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
            updatedAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10)
        ),
        createdAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
        updatedAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10)
    )

    private val baseVehicleParkingSessionEntity = VehicleParkingSessionEntity(
        id = 1,
        licensePlate = "ABC1234",
        status = PARKED,
        entryTime = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
        parkedTime = null,
        exitTime = null,
        totalPrice = null,
        parkingSpotEntity = ParkingSpotEntity(
            id = 1,
            lat = 10.0,
            lng = 20.0,
            occupied = false,
            garageSectorEntity = GarageSectorEntity(
                id = 1,
                sectorCode = "A",
                basePrice = BigDecimal("40.50"),
                maxCapacity = 10,
                openHour = LocalTime.of(0, 0),
                closeHour = LocalTime.of(23, 59),
                durationLimitMinutes = 1440,
                availableSpots = 5,
                createdAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
                updatedAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
            ),
            createdAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
            updatedAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10)
        ),
        createdAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10),
        updatedAt = LocalDateTime.of(2021, 10, 10, 10, 10, 10)
    )

    @Test
    fun `findByLicensePlateWithStatusEntryOrParked should return VehicleParkingSession when found`() {
        every { vehicleParkingSessionRepositoryJpa.findByLicensePlateAndStatusEntryOrParked("ABC1234") } returns baseVehicleParkingSessionEntity

        val result =
            vehicleParkingSessionRepository.findByLicensePlateWithStatusEntryOrParked(baseVehicleParkingSession)

        assertEquals(baseVehicleParkingSession, result)
        verify(exactly = 1) { vehicleParkingSessionRepositoryJpa.findByLicensePlateAndStatusEntryOrParked("ABC1234") }
    }

    @Test
    fun `findByLicensePlateWithStatusEntryOrParked should return null when not found`() {
        every { vehicleParkingSessionRepositoryJpa.findByLicensePlateAndStatusEntryOrParked("XYZ5678") } returns null

        val result = vehicleParkingSessionRepository.findByLicensePlateWithStatusEntryOrParked(
            baseVehicleParkingSession.copy(licensePlate = "XYZ5678")
        )

        assertNull(result)
        verify(exactly = 1) { vehicleParkingSessionRepositoryJpa.findByLicensePlateAndStatusEntryOrParked("XYZ5678") }
    }

    @Test
    fun `sumByStatusExitedAndSectorAndDate should return total price`() {
        every {
            vehicleParkingSessionRepositoryJpa.sumByStatusExitedAndSectorAndDate(
                LocalDate.of(2023, 1, 1),
                "A"
            )
        } returns BigDecimal("100.00")

        val result = vehicleParkingSessionRepository.sumByStatusExitedAndSectorAndDate(LocalDate.of(2023, 1, 1), "A")

        assertEquals(BigDecimal("100.00"), result)
        verify(exactly = 1) {
            vehicleParkingSessionRepositoryJpa.sumByStatusExitedAndSectorAndDate(
                LocalDate.of(
                    2023,
                    1,
                    1
                ), "A"
            )
        }
    }

    @Test
    fun `save should persist and return VehicleParkingSession`() {
        every { vehicleParkingSessionRepositoryJpa.save(any()) } returns baseVehicleParkingSessionEntity

        val result = vehicleParkingSessionRepository.save(baseVehicleParkingSession)

        assertEquals(baseVehicleParkingSession, result)
        verify(exactly = 1) { vehicleParkingSessionRepositoryJpa.save(baseVehicleParkingSessionEntity) }
    }
}