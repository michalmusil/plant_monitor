package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Json
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPlantViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val plantsRepository: IPlantsRepository
): ViewModel() {
    val uiState: MutableState<AddPlantUiState> = mutableStateOf(AddPlantUiState.Start())

    fun savePlant(
        name: String,
        species: String,
        description: String?,
        measurementValueLimits: List<MeasurementValueLimit>?,
        plantImage: Bitmap?
    ){
        val userId = authenticationManager.getUserId()
        val newPlant = PostPlant(
            name = name,
            species = species,
            userId = userId,
            description = description,
            measurementValueLimits = measurementValueLimits
        )
        viewModelScope.launch{
            val result = plantsRepository.postNewPlant(newPlant)

            when(result){
                is CommunicationResult.Success -> {
                    plantImage?.let {
                        savePlantImage(it)
                    }
                    uiState.value = AddPlantUiState.PlantSaved(result.data)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = AddPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
                is CommunicationResult.Error -> {
                    uiState.value = AddPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
            }
        }
    }

    private suspend fun savePlantImage(plantImage: Bitmap){
        // TODO
    }

}