package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

sealed class PlantsUiState{
    class Start(): PlantsUiState()
    class PlantsLoaded(val plants: List<GetPlant>): PlantsUiState()
    class Error(val errorStringCode: Int): PlantsUiState()
}