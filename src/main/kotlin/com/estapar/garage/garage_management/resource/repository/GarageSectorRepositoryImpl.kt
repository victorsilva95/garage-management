package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.repository.GarageSectorRepository
import com.estapar.garage.garage_management.resource.repository.entity.GarageSectorEntity
import org.springframework.stereotype.Repository

@Repository
class GarageSectorRepositoryImpl(
    val garageSectorRepositoryJpa: GarageSectorRepositoryJpa
) : GarageSectorRepository {

    override fun save(garageSector: GarageSector): GarageSector =
        garageSectorRepositoryJpa.save(GarageSectorEntity.fromDomain(garageSector))
            .toDomain()

    override fun sumAllAvailableSpots(): Int = garageSectorRepositoryJpa.sumAllAvailableSpots()

    override fun findBySectorCode(sectorCode: String): GarageSector? =
        garageSectorRepositoryJpa.findBySectorCode(sectorCode)?.toDomain()


}