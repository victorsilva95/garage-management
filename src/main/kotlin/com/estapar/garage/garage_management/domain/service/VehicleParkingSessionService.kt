package com.estapar.garage.garage_management.domain.service

import com.estapar.garage.garage_management.domain.entity.PlateStatusInfo
import com.estapar.garage.garage_management.domain.entity.RevenueInfo
import com.estapar.garage.garage_management.domain.entity.SpotStatusInfo
import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import java.time.LocalDate

interface VehicleParkingSessionService {
    fun saveEntryStatus(vehicleParkingSession: VehicleParkingSession)
    fun saveParkedStatus(vehicleParkingSession: VehicleParkingSession, lat: Double, lng: Double)
    fun saveExitStatus(vehicleParkingSession: VehicleParkingSession)
    fun getPlaceStatus(licensePlate: String): PlateStatusInfo
    fun getSpotStatus(lat: Double, lng: Double): SpotStatusInfo
    fun getRevenue(date: LocalDate, sector: String): RevenueInfo
}