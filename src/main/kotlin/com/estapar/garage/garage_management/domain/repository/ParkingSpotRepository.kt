package com.estapar.garage.garage_management.domain.repository

import com.estapar.garage.garage_management.domain.entity.ParkingSpot

interface ParkingSpotRepository {
    fun findFullByLatAndLng(lat: Double, lng: Double): ParkingSpot?
    fun save(parkingSpot: ParkingSpot): ParkingSpot
}