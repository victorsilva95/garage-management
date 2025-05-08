package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.resource.repository.entity.GarageSectorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GarageSectorRepositoryJpa : JpaRepository<GarageSectorEntity, Long> {
    @Query("select sum(gs.availableSpots) from GarageSector gs")
    fun sumAllAvailableSpots(): Int

    @Query("select gs from GarageSector gs where gs.sectorCode = :sectorCode")
    fun findBySectorCode(sectorCode: String): GarageSectorEntity?
}