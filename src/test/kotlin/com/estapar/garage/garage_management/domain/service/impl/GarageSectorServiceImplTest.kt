package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.repository.GarageSectorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalTime

class GarageSectorServiceImplTest {

    private val garageSectorRepository: GarageSectorRepository = mockk()
    private val garageSectorService = GarageSectorServiceImpl(garageSectorRepository)

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

    @Test
    fun `decrementAvailableSectors should decrement available spots and save`() {
        val updatedGarageSector = baseGarageSector.copy(availableSpots = 4)

        every { garageSectorRepository.save(any()) } returns updatedGarageSector

        val result = garageSectorService.decrementAvailableSectors(baseGarageSector)

        assertEquals(4, result.availableSpots)
        verify(exactly = 1) { garageSectorRepository.save(any()) }
    }

    @Test
    fun `decrementAvailableSectors should throw exception when garageSector is null`() {
        assertThrows<IllegalArgumentException> {
            garageSectorService.decrementAvailableSectors(null)
        }
    }

    @Test
    fun `incrementAvailableSectors should increment available spots and save`() {
        val updatedGarageSector = baseGarageSector.copy(availableSpots = 6)

        every { garageSectorRepository.save(any()) } returns updatedGarageSector

        val result = garageSectorService.incrementAvailableSectors(baseGarageSector)

        assertEquals(6, result.availableSpots)
        verify(exactly = 1) { garageSectorRepository.save(any()) }
    }

    @Test
    fun `incrementAvailableSectors should throw exception when garageSector is null`() {
        assertThrows<IllegalArgumentException> {
            garageSectorService.incrementAvailableSectors(null)
        }
    }

    @Test
    fun `checkAvailableSpots should return true when there are available spots`() {
        every { garageSectorRepository.sumAllAvailableSpots() } returns 5

        val result = garageSectorService.checkAvailableSpots()

        assertTrue(result)
        verify(exactly = 1) { garageSectorRepository.sumAllAvailableSpots() }
    }

    @Test
    fun `checkAvailableSpots should return false when there are no available spots`() {
        every { garageSectorRepository.sumAllAvailableSpots() } returns 0

        val result = garageSectorService.checkAvailableSpots()

        assertFalse(result)
        verify(exactly = 1) { garageSectorRepository.sumAllAvailableSpots() }
    }

    @Test
    fun `findBySectorCode should return garage sector when found`() {
        every { garageSectorRepository.findBySectorCode("A") } returns baseGarageSector

        val result = garageSectorService.findBySectorCode("A")

        assertNotNull(result)
        assertEquals("A", result?.sectorCode)
        verify(exactly = 1) { garageSectorRepository.findBySectorCode("A") }
    }

    @Test
    fun `findBySectorCode should return null when not found`() {
        every { garageSectorRepository.findBySectorCode("B") } returns null

        val result = garageSectorService.findBySectorCode("B")

        assertNull(result)
        verify(exactly = 1) { garageSectorRepository.findBySectorCode("B") }
    }

    @Test
    fun `save should save and return the garage sector`() {
        every { garageSectorRepository.save(baseGarageSector) } returns baseGarageSector

        val result = garageSectorService.save(baseGarageSector)

        assertNotNull(result)
        assertEquals(baseGarageSector, result)
        verify(exactly = 1) { garageSectorRepository.save(baseGarageSector) }
    }
}