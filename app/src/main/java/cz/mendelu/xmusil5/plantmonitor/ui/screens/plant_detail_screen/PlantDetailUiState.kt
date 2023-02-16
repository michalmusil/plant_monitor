package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

sealed class PlantDetailUiState{
    class Start(): PlantDetailUiState()
    class Error(val errorStringCode: Int): PlantDetailUiState()
}