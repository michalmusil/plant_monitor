package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

sealed class AddOrEditPlantMode{
    class NewPlant(): AddOrEditPlantMode()
    class EditPlant(val plant: GetPlant): AddOrEditPlantMode()
}
