package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_or_edit_plant_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant

sealed class AddOrEditPlantMode{
    class NewPlant(): AddOrEditPlantMode()
    class EditPlant(): AddOrEditPlantMode()
}
