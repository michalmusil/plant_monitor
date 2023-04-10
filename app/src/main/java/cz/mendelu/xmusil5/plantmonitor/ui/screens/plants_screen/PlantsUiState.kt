package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant

sealed class PlantsUiState{
    class Start(): PlantsUiState()
    class NoPlantsYet(): PlantsUiState()
    class PlantsLoaded(val plants: List<Plant>): PlantsUiState()
    class Error(val errorStringCode: Int): PlantsUiState()
}