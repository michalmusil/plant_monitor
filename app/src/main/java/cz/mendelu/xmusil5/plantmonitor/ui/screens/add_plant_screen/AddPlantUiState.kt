package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

sealed class AddPlantUiState{
    class Start(): AddPlantUiState()
    class Error(val errorStringCode: Int): AddPlantUiState()
}
