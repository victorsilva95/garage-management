package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.resource.repository.entity.ParkingSpotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ParkingSpotRepositoryJpa : JpaRepository<ParkingSpotEntity, Long> {

    @Query("select ps FROM ParkingSpot ps " +
            "JOIN FETCH ps.garageSectorEntity gs WHERE ps.lat = :lat and ps.lng = :lng")
    fun findFullByLatAndLng(lat: Double, lng: Double): ParkingSpotEntity?

    @Query("select ps FROM ParkingSpot ps " +
            "WHERE ps.lat = :lat and ps.lng = :lng")
    fun findByLatAndLng(lat: Double, lng: Double): ParkingSpotEntity?
}