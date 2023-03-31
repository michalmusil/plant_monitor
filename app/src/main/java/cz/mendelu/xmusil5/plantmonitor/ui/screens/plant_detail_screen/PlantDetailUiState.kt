package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

sealed class PlantDetailUiState{
    class Start: PlantDetailUiState()
    class PlantLoaded: PlantDetailUiState()
    class MeasurementsLoaded: PlantDetailUiState()
    class Error(val errorStringCode: Int): PlantDetailUiState()
}