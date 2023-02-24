package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

sealed class AddOrEditPlantUiState{
    class Start(): AddOrEditPlantUiState()
    class PlantToEditLoaded(val plant: GetPlant): AddOrEditPlantUiState()
    class PlantSaved(val plant: GetPlant): AddOrEditPlantUiState()
    class PlantPostFailed(val reasonStringCode: Int): AddOrEditPlantUiState()
    class Error(val errorStringCode: Int): AddOrEditPlantUiState()
}
