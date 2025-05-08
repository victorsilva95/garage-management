package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.resource.repository.entity.GarageSectorEntity
import com.estapar.garage.garage_management.resource.repository.entity.ParkingSpotEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

class ParkingSpotRepositoryImplTest {

    private val parkingSpotRepositoryJpa: ParkingSpotRepositoryJpa = mockk()
    private val parkingSpotRepository = ParkingSpotRepositoryImpl(parkingSpotRepositoryJpa)

    private val baseParkingSpot = ParkingSpot(
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
    )

    private val baseParkingSpotEntity = ParkingSpotEntity(
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
    )

    @Test
    fun `findFullByLatAndLng should return ParkingSpot when found`() {
        every { parkingSpotRepositoryJpa.findFullByLatAndLng(10.0, 20.0) } returns baseParkingSpotEntity

        val result = parkingSpotRepository.findFullByLatAndLng(10.0, 20.0)

        assertEquals(baseParkingSpot, result)
        verify(exactly = 1) { parkingSpotRepositoryJpa.findFullByLatAndLng(10.0, 20.0) }
    }

    @Test
    fun `findFullByLatAndLng should return null when not found`() {
        every { parkingSpotRepositoryJpa.findFullByLatAndLng(30.0, 40.0) } returns null

        val result = parkingSpotRepository.findFullByLatAndLng(30.0, 40.0)

        assertNull(result)
        verify(exactly = 1) { parkingSpotRepositoryJpa.findFullByLatAndLng(30.0, 40.0) }
    }

    @Test
    fun `save should persist and return ParkingSpot`() {
        every { parkingSpotRepositoryJpa.save(any()) } returns baseParkingSpotEntity

        val result = parkingSpotRepository.save(baseParkingSpot)

        assertEquals(baseParkingSpot, result)
        verify(exactly = 1) { parkingSpotRepositoryJpa.save(baseParkingSpotEntity) }
    }
}