package com.estapar.garage.garage_management.aplication.web.controller

import com.estapar.garage.garage_management.aplication.web.controller.request.*
import com.estapar.garage.garage_management.aplication.web.controller.response.PostParkedStatusResponse
import com.estapar.garage.garage_management.aplication.web.controller.response.PostRevenueResponse
import com.estapar.garage.garage_management.aplication.web.controller.response.PostSpotStatusResponse
import com.estapar.garage.garage_management.domain.service.SetupGarageService
import com.estapar.garage.garage_management.domain.service.VehicleParkingSessionService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/garage")
@Tag(name = "Garage")
class GarageController(
    val vehicleParkingSessionService: VehicleParkingSessionService,
    val setupGarageService: SetupGarageService
) {
    @PostMapping("/entry")
    fun postEntryStatus(@Valid @RequestBody postEntryStatusRequest: PostEntryStatusRequest): ResponseEntity<Void> {
        vehicleParkingSessionService.saveEntryStatus(postEntryStatusRequest.toDomain())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/parked")
    fun postParkedStatus(
        @Valid @RequestBody postParkedStatusRequest: PostParkedStatusRequest
    ): ResponseEntity<Void> {
        vehicleParkingSessionService.saveParkedStatus(
            postParkedStatusRequest.toDomain(),
            postParkedStatusRequest.lat,
            postParkedStatusRequest.lng
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/exit")
    fun postExitStatus(@Valid @RequestBody postExitStatusRequest: PostExitStatusRequest): ResponseEntity<Void> {
        vehicleParkingSessionService.saveExitStatus(postExitStatusRequest.toDomain())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/plate-status")
    fun postPlateStatus(
        @Valid @RequestBody postPlateStatusRequest: PostPlateStatusRequest
    ): ResponseEntity<PostParkedStatusResponse> {
        val plateStatusInfo = vehicleParkingSessionService.getPlaceStatus(
            postPlateStatusRequest.licensePlate
        )
        return ResponseEntity.ok(PostParkedStatusResponse.fromDomain(plateStatusInfo))
    }

    @PostMapping("/spot-status")
    fun postSpotStatus(
        @Valid @RequestBody postSpotStatusRequest: PostSpotStatusRequest
    ): ResponseEntity<PostSpotStatusResponse> {
        val spotStatusInfo = vehicleParkingSessionService.getSpotStatus(
            postSpotStatusRequest.lat,
            postSpotStatusRequest.lng
        )
        return ResponseEntity.ok(PostSpotStatusResponse.fromDomain(spotStatusInfo))
    }

    @GetMapping("/revenue")
    fun getRevenue(
        @Valid @RequestBody postRevenueRequest: PostRevenueRequest
    ): ResponseEntity<PostRevenueResponse> {
        val revenueInfo = vehicleParkingSessionService.getRevenue(
            postRevenueRequest.date, postRevenueRequest.sector
        )
        return ResponseEntity.ok(PostRevenueResponse.fromDomain(revenueInfo))
    }

    @GetMapping
    fun initialSetupGarage(
        @Valid @RequestBody getInitialSetupGarageRequest: GetInitialSetupGarageRequest
    ): ResponseEntity<Void> {
        setupGarageService.createInitialSetupGarage(getInitialSetupGarageRequest.toDomain())
        return ResponseEntity.ok().build()
    }
}
