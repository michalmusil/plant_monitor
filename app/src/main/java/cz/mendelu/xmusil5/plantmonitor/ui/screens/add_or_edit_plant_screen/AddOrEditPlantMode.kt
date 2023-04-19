package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_or_edit_plant_screen

sealed class AddOrEditPlantMode{
    class NewPlant(): AddOrEditPlantMode()
    class EditPlant(): AddOrEditPlantMode()
}
