package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_or_edit_plant_screen

sealed class AddOrEditPlantUiState{
    class Start(): AddOrEditPlantUiState()
    class PlantToEditLoaded(): AddOrEditPlantUiState()
    class SavingChanges(): AddOrEditPlantUiState()
    class PlantSaved(): AddOrEditPlantUiState()
    class PlantPostFailed(val reasonStringCode: Int): AddOrEditPlantUiState()
    class PlantDeleted(): AddOrEditPlantUiState()
    class Error(val errorStringCode: Int): AddOrEditPlantUiState()
}
