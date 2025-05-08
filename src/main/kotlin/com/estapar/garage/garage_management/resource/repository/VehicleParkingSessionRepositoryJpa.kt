package com.estapar.garage.garage_management.resource.repository

import com.estapar.garage.garage_management.resource.repository.entity.VehicleParkingSessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal
import java.time.LocalDate

interface VehicleParkingSessionRepositoryJpa : JpaRepository<VehicleParkingSessionEntity, Long> {

    @Query(
        "select vps FROM VehicleParkingSession vps where vps.licensePlate = :licensePlate " +
                "AND (vps.status = 'ENTRY' OR vps.status = 'PARKED')"
    )
    fun findByLicensePlateAndStatusEntryOrParked(licensePlate: String): VehicleParkingSessionEntity?

    @Query(
        "select vps FROM VehicleParkingSession vps where vps.licensePlate = :licensePlate " +
                "AND vps.status = 'ENTRY'"
    )
    fun findByStatusEntry(licensePlate: String): VehicleParkingSessionEntity?

    @Query(
        "select vps FROM VehicleParkingSession vps " +
                "JOIN FETCH vps.parkingSpotEntity ps " +
                "JOIN FETCH ps.garageSectorEntity gs " +
                "where vps.licensePlate = :licensePlate " +
                "AND (vps.status = 'ENTRY' OR vps.status = 'PARKED')"
    )
    fun findFullByStatusEntryOrParked(licensePlate: String): VehicleParkingSessionEntity?

    @Query(
        "select vps FROM VehicleParkingSession vps " +
                "JOIN FETCH vps.parkingSpotEntity ps " +
                "JOIN FETCH ps.garageSectorEntity gs " +
                "where ps.lat = :lat " +
                "AND ps.lng = :lng " +
                "AND vps.status = 'PARKED'"
    )
    fun findFullByLatLngAndStatusParked(lat: Double, lng: Double): VehicleParkingSessionEntity?

    @Query(
        "SELECT SUM(vps.total_price) " +
                "FROM vehicle_parking_session vps " +
                "JOIN parking_spot ps ON vps.spot_id = ps.id " +
                "JOIN garage_sector gs ON ps.sector_id = gs.id " +
                "WHERE gs.sector_code = :sector " +
                "AND DATE(vps.exit_time) = :date " +
                "AND vps.status = 'EXITED'", nativeQuery = true
    )
    fun sumByStatusExitedAndSectorAndDate(date: LocalDate, sector: String): BigDecimal?
}