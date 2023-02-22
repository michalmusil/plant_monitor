package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import androidx.lifecycle.ViewModel
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository
): ViewModel() {
}