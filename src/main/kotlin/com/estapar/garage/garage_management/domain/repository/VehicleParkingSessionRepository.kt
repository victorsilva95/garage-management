package com.estapar.garage.garage_management.domain.repository

import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import java.math.BigDecimal
import java.time.LocalDate

interface VehicleParkingSessionRepository {
    fun findByLicensePlateWithStatusEntryOrParked(vehicleParkingSession: VehicleParkingSession): VehicleParkingSession?
    fun findByStatusEntry(vehicleParkingSession: VehicleParkingSession): VehicleParkingSession?
    fun findFullByStatusEntryOrParked(licensePlate: String): VehicleParkingSession?
    fun findFullByLatLngAndStatusParked(lat: Double, lng: Double): VehicleParkingSession?
    fun sumByStatusExitedAndSectorAndDate(date: LocalDate, sector: String): BigDecimal?
    fun save(vehicleParkingSession: VehicleParkingSession): VehicleParkingSession
}