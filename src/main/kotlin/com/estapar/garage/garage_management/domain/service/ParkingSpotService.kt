package com.estapar.garage.garage_management.domain.service

import com.estapar.garage.garage_management.domain.entity.GarageSector
import com.estapar.garage.garage_management.domain.entity.ParkingSpot

interface ParkingSpotService {
    fun markSpotAsOccupied(lat: Double, lng: Double): ParkingSpot
    fun markSpotAsUnoccupied(lat: Double, lng: Double): GarageSector
    fun findFullByLatAndLng(lat: Double, lng: Double): ParkingSpot?
    fun save(parkingSpot: ParkingSpot): ParkingSpot
}