package com.estapar.garage.garage_management.domain.service

import com.estapar.garage.garage_management.domain.entity.SetupGarageInitialInfo

interface SetupGarageService {
    fun createInitialSetupGarage(setupGarageInitialInfo: SetupGarageInitialInfo)
}