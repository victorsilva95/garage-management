package com.estapar.garage.garage_management.domain.service.impl

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.repository.GarageSectorRepository
import com.estapar.garage.garage_management.domain.service.GarageSectorService
import org.springframework.stereotype.Service

@Service
class GarageSectorServiceImpl(val garageSectorRepository: GarageSectorRepository) : GarageSectorService {

    override fun decrementAvailableSectors(garageSector: GarageSector?): GarageSector {
        requireNotNull(garageSector)
        return this.save(garageSector.decrementAvailableSpots())
    }

    override fun incrementAvailableSectors(garageSector: GarageSector?): GarageSector {
        requireNotNull(garageSector)
        return this.save(garageSector.incrementAvailableSpots())
    }

    override fun checkAvailableSpots(): Boolean =
        garageSectorRepository.sumAllAvailableSpots() > 0

    override fun findBySectorCode(sectorCode: String): GarageSector? =
        garageSectorRepository.findBySectorCode(sectorCode)

    override fun save(garageSector: GarageSector) =
        garageSectorRepository.save(garageSector)
}