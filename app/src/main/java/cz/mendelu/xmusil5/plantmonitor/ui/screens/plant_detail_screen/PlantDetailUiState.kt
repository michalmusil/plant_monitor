package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

sealed class PlantDetailUiState{
    class Start(): PlantDetailUiState()
    class PlantLoaded(val plant: GetPlant): PlantDetailUiState()
    class MeasurementsLoaded(val measurements: List<GetMeasurement>): PlantDetailUiState()
    class Error(val errorStringCode: Int): PlantDetailUiState()
}