package com.estapar.garage.garage_management.domain.service

import com.estapar.garage.garage_management.domain.entity.GarageSector

interface GarageSectorService {
    fun decrementAvailableSectors(garageSector: GarageSector?): GarageSector
    fun incrementAvailableSectors(garageSector: GarageSector?): GarageSector
    fun checkAvailableSpots(): Boolean
    fun findBySectorCode(sectorCode: String): GarageSector?
    fun save(garageSector: GarageSector): GarageSector
}