package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

sealed class AddPlantUiState{
    class Start(): AddPlantUiState()
    class PlantSaved(val plant: GetPlant): AddPlantUiState()
    class PlantPostFailed(val reasonStringCode: Int): AddPlantUiState()
    class Error(val errorStringCode: Int): AddPlantUiState()
}
