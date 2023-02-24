package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Json
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.CommunicationConstants.HOUSE_PLANT_MEASUREMENTS_API_IMAGE_UPLOAD_FORM_PART_NAME
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.utils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AddPlantViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val plantsRepository: IPlantsRepository
): ViewModel() {
    val uiState: MutableState<AddPlantUiState> = mutableStateOf(AddPlantUiState.Start())

    fun savePlant(
        context: Context,
        name: String,
        species: String,
        description: String?,
        measurementValueLimits: List<MeasurementValueLimit>?,
        plantImageUri: Uri?
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
                    plantImageUri?.let {
                        async {
                            savePlantImage(
                                context = context,
                                plantId = result.data.id,
                                imageUri = it
                            )
                        }.await()
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

    private suspend fun savePlantImage(context: Context, plantId: Long, imageUri: Uri){
        val contentResolver = context.contentResolver
        try {
            val stream = contentResolver.openInputStream(imageUri)
            stream?.let {
                val imageName = ImageUtils.getFileName(context, imageUri)
                val mediaType = contentResolver.getType(imageUri)?.toMediaTypeOrNull()

                val imageInBytes = it.readBytes()
                val request = imageInBytes.toRequestBody(mediaType, 0, imageInBytes.size)
                val filePart = MultipartBody.Part.createFormData(
                    HOUSE_PLANT_MEASUREMENTS_API_IMAGE_UPLOAD_FORM_PART_NAME,
                    imageName,
                    request
                )
                plantsRepository.uploadPlantImage(
                    plantId = plantId,
                    imagePart = filePart
                )
            }
        }
        catch (ex: java.lang.Exception){
            ex.printStackTrace()
        }
    }
}