package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.ParkingSpot
import com.estapar.garage.garage_management.domain.repository.ParkingSpotRepository
import com.estapar.garage.garage_management.domain.service.GarageSectorService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import java.math.BigDecimal
import java.time.LocalTime

class ParkingSpotServiceImplTest {

    private val parkingSpotRepository: ParkingSpotRepository = mockk()
    private val garageSectorService: GarageSectorService = mockk()
    private val parkingSpotService = ParkingSpotServiceImpl(parkingSpotRepository, garageSectorService)

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

    @Test
    fun `markSpotAsOccupied should mark spot as occupied and decrement available spots`() {
        every { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) } returns baseParkingSpot
        every { parkingSpotRepository.save(any()) } returns baseParkingSpot.copy(occupied = true)
        every { garageSectorService.decrementAvailableSectors(any()) } returns baseGarageSector.copy(availableSpots = 4)

        val result = parkingSpotService.markSpotAsOccupied(10.0, 20.0)

        assertTrue(result.occupied == true)
        verify(exactly = 1) { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) }
        verify(exactly = 1) { parkingSpotRepository.save(any()) }
        verify(exactly = 1) { garageSectorService.decrementAvailableSectors(any()) }
    }

    @Test
    fun `markSpotAsOccupied should throw exception when spot is already occupied`() {
        val occupiedSpot = baseParkingSpot.copy(occupied = true)
        every { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) } returns occupiedSpot

        val exception = assertThrows<HttpClientErrorException> {
            parkingSpotService.markSpotAsOccupied(10.0, 20.0)
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("422 Spot is already occupied", exception.message)
        verify(exactly = 1) { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) }
        verify(exactly = 0) { parkingSpotRepository.save(any()) }
        verify(exactly = 0) { garageSectorService.decrementAvailableSectors(any()) }
    }

    @Test
    fun `markSpotAsOccupied should throw exception when spot is not found`() {
        every { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) } returns null

        val exception = assertThrows<HttpClientErrorException> {
            parkingSpotService.markSpotAsOccupied(10.0, 20.0)
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("422 Spot not found", exception.message)
        verify(exactly = 1) { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) }
        verify(exactly = 0) { parkingSpotRepository.save(any()) }
        verify(exactly = 0) { garageSectorService.decrementAvailableSectors(any()) }
    }

    @Test
    fun `markSpotAsUnoccupied should mark spot as unoccupied and increment available spots`() {
        every { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) } returns baseParkingSpot
        every { parkingSpotRepository.save(any()) } returns baseParkingSpot.copy(occupied = false)
        every { garageSectorService.incrementAvailableSectors(any()) } returns baseGarageSector.copy(availableSpots = 6)

        val result = parkingSpotService.markSpotAsUnoccupied(10.0, 20.0)

        assertEquals(6, result.availableSpots)
        verify(exactly = 1) { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) }
        verify(exactly = 1) { parkingSpotRepository.save(any()) }
        verify(exactly = 1) { garageSectorService.incrementAvailableSectors(any()) }
    }

    @Test
    fun `markSpotAsUnoccupied should throw exception when spot is not found`() {
        every { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) } returns null

        val exception = assertThrows<HttpClientErrorException> {
            parkingSpotService.markSpotAsUnoccupied(10.0, 20.0)
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("422 Spot not found", exception.message)
        verify(exactly = 1) { parkingSpotRepository.findFullByLatAndLng(10.0, 20.0) }
        verify(exactly = 0) { parkingSpotRepository.save(any()) }
        verify(exactly = 0) { garageSectorService.incrementAvailableSectors(any()) }
    }
}