package com.estapar.garage.garage_management.domain.entity

data class SetupGarageInitialInfo(
    val garageSector: List<GarageSector>,
    val initialSetupSpotInfo: List<InitialSetupSpotInfo>
)
