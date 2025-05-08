package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.resource.repository.entity.GarageSectorEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

class GarageSectorRepositoryImplTest {

    private val garageSectorRepositoryJpa: GarageSectorRepositoryJpa = mockk()
    private val garageSectorRepository = GarageSectorRepositoryImpl(garageSectorRepositoryJpa)

    private val baseGarageSector = GarageSector(
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
    )

    private val baseGarageSectorEntity = GarageSectorEntity(
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
    )

    @Test
    fun `save should persist and return GarageSector`() {
        every { garageSectorRepositoryJpa.save(any()) } returns baseGarageSectorEntity

        val result = garageSectorRepository.save(baseGarageSector)

        assertEquals(baseGarageSector, result)
        verify(exactly = 1) { garageSectorRepositoryJpa.save(baseGarageSectorEntity) }
    }

    @Test
    fun `sumAllAvailableSpots should return total available spots`() {
        every { garageSectorRepositoryJpa.sumAllAvailableSpots() } returns 15

        val result = garageSectorRepository.sumAllAvailableSpots()

        assertEquals(15, result)
        verify(exactly = 1) { garageSectorRepositoryJpa.sumAllAvailableSpots() }
    }

    @Test
    fun `findBySectorCode should return GarageSector when found`() {
        every { garageSectorRepositoryJpa.findBySectorCode("A") } returns baseGarageSectorEntity

        val result = garageSectorRepository.findBySectorCode("A")

        assertEquals(baseGarageSector, result)
        verify(exactly = 1) { garageSectorRepositoryJpa.findBySectorCode("A") }
    }

    @Test
    fun `findBySectorCode should return null when not found`() {
        every { garageSectorRepositoryJpa.findBySectorCode("B") } returns null

        val result = garageSectorRepository.findBySectorCode("B")

        assertEquals(null, result)
        verify(exactly = 1) { garageSectorRepositoryJpa.findBySectorCode("B") }
    }
}