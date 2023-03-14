package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_or_edit_plant_screen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.ApiConstants.HOUSE_PLANT_MEASUREMENTS_API_IMAGE_UPLOAD_FORM_PART_NAME
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimitInEdit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.utils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AddOrEditPlantViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val plantsRepository: IPlantsRepository
): ViewModel() {

    val uiState: MutableState<AddOrEditPlantUiState> = mutableStateOf(AddOrEditPlantUiState.Start())
    val mode: MutableState<AddOrEditPlantMode> = mutableStateOf(AddOrEditPlantMode.NewPlant())

    fun saveNewPlant(
        context: Context,
        name: String,
        species: String,
        description: String?,
        measurementValueLimits: List<MeasurementValueLimitInEdit>?,
        plantImageUri: Uri?
    ){
        val userId = authenticationManager.getUserId()
        val limitsToSave = measurementValueLimits?.filter {
            it.enabled
        }?.map {
            it.limit
        }
        val newPlant = PostPlant(
            name = name,
            species = species,
            userId = userId,
            description = description,
            measurementValueLimits = limitsToSave
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
                    uiState.value = AddOrEditPlantUiState.PlantSaved(result.data)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
                is CommunicationResult.Error -> {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
            }
        }
    }

    fun updateExistingPlant(
        context: Context,
        existingPlant: GetPlant,
        name: String,
        species: String,
        description: String?,
        measurementValueLimits: List<MeasurementValueLimitInEdit>?,
        plantImageUri: Uri?
    ){
        val plantId = existingPlant.id
        val limitsToSave = measurementValueLimits?.filter {
            it.enabled
        }?.map {
            it.limit
        }
        val updatedPlant = PutPlant(
            id = plantId,
            name = name,
            species = species,
            description = description,
            measurementValueLimits = limitsToSave
        )
        viewModelScope.launch{
            val result = plantsRepository.updatePlant(updatedPlant)

            when(result){
                is CommunicationResult.Success -> {
                    if (plantImageUri != null && plantImageUri != Uri.EMPTY) {
                        async {
                            savePlantImage(
                                context = context,
                                plantId = result.data.id,
                                imageUri = plantImageUri
                            )
                        }.await()
                    }
                    uiState.value = AddOrEditPlantUiState.PlantSaved(result.data)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
                is CommunicationResult.Error -> {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
            }
        }
    }

    fun deletePlant(plant: GetPlant){
        viewModelScope.launch {
            val result = plantsRepository.deletePlant(plantId = plant.id)
            if (result is CommunicationResult.Success){
                uiState.value = AddOrEditPlantUiState.PlantDeleted()
            }
            else {
                uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.failedToDeletePlant)
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

    fun fetchPlant(plantId: Long){
        viewModelScope.launch {
            val plantCall = plantsRepository.getPlantById(plantId)

            when(plantCall){
                is CommunicationResult.Success -> {
                    val resultPlant = plantCall.data
                    if (resultPlant.hasTitleImage){
                        resultPlant.titleImageBitmap = fetchPlantImage(resultPlant)
                    }
                    mode.value = AddOrEditPlantMode.EditPlant(plant = resultPlant)
                    uiState.value = AddOrEditPlantUiState.PlantToEditLoaded(plant = resultPlant)
                }
                is CommunicationResult.Error -> {
                    uiState.value = AddOrEditPlantUiState.Error(R.string.somethingWentWrong)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = AddOrEditPlantUiState.Error(R.string.connectionError)
                }
            }
        }
    }

    private suspend fun fetchPlantImage(plant: GetPlant): Bitmap?{
        val result = plantsRepository.getPlantImage(plantId = plant.id)
        if (result is CommunicationResult.Success){
            return result.data
        }
        return null
    }

    fun getMeasurementValueLimitsForEditing(
        plantLimits: List<MeasurementValueLimit>
    ): List<MeasurementValueLimitInEdit>{
        val limitForms = mutableListOf<MeasurementValueLimitInEdit>()
        MeasurementType.getValidTypes().forEach { type ->
            val limit = plantLimits.firstOrNull{ it.type == type }
            if (limit != null){
                limitForms.add(
                    MeasurementValueLimitInEdit(
                        enabled = true,
                        limit = limit
                    )
                )
            }
            else {
                limitForms.add(
                    MeasurementValueLimitInEdit(
                        enabled = false,
                        limit = MeasurementValueLimit(
                            type = type,
                            lowerLimit = type.minLimit,
                            upperLimit = type.maxLimit
                        )
                    )
                )
            }
        }
        return limitForms
    }
}