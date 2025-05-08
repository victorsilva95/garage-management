package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.domain.entity.VehicleParkingSession
import com.estapar.garage.garage_management.domain.repository.VehicleParkingSessionRepository
import com.estapar.garage.garage_management.resource.repository.entity.VehicleParkingSessionEntity
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class VehicleParkingSessionRepositoryImpl(
    val vehicleParkingSessionRepositoryJpa: VehicleParkingSessionRepositoryJpa
) : VehicleParkingSessionRepository {

    override fun findByLicensePlateWithStatusEntryOrParked(
        vehicleParkingSession: VehicleParkingSession
    ): VehicleParkingSession? =
        vehicleParkingSessionRepositoryJpa.findByLicensePlateAndStatusEntryOrParked(
            vehicleParkingSession.licensePlate
        )?.toDomain()

    override fun findByStatusEntry(vehicleParkingSession: VehicleParkingSession) =
        vehicleParkingSessionRepositoryJpa.findByStatusEntry(
            vehicleParkingSession.licensePlate
        )?.toDomain()

    override fun findFullByStatusEntryOrParked(licensePlate: String): VehicleParkingSession? =
        vehicleParkingSessionRepositoryJpa.findFullByStatusEntryOrParked(licensePlate)?.toDomain()

    override fun findFullByLatLngAndStatusParked(lat: Double, lng: Double): VehicleParkingSession? =
        vehicleParkingSessionRepositoryJpa.findFullByLatLngAndStatusParked(lat, lng)?.toDomain()

    override fun sumByStatusExitedAndSectorAndDate(
        date: LocalDate,
        sector: String
    ): BigDecimal? =
        vehicleParkingSessionRepositoryJpa.sumByStatusExitedAndSectorAndDate(date, sector)

    override fun save(vehicleParkingSession: VehicleParkingSession): VehicleParkingSession =
        vehicleParkingSessionRepositoryJpa.save(VehicleParkingSessionEntity.fromDomain(vehicleParkingSession))
            .toDomain()
}