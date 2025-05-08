package com.estapar.garage.garage_management.domain.repository

import com.estapar.garage.garage_management.domain.entity.GarageSector

interface GarageSectorRepository {
    fun save(garageSector: GarageSector): GarageSector
    fun sumAllAvailableSpots(): Int
    fun findBySectorCode(sectorCode: String): GarageSector?
}