package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

sealed class PlantsUiState{
    class Start(): PlantsUiState()
    class Error(val errorStringCode: Int): PlantsUiState()
}