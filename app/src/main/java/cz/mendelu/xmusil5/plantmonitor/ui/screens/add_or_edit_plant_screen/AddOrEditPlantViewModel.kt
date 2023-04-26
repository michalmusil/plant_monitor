package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_or_edit_plant_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.ApiConstants.HOUSE_PLANT_MEASUREMENTS_API_IMAGE_UPLOAD_FORM_PART_NAME
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimitInEdit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.models.support.BitmapWithUri
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AddOrEditPlantViewModel @Inject constructor(
    private val userSessionManager: IUserSessionManager,
    private val plantsRepository: IPlantsRepository
): ViewModel() {

    val uiState: MutableState<AddOrEditPlantUiState> = mutableStateOf(AddOrEditPlantUiState.Start())
    val mode: MutableState<AddOrEditPlantMode> = mutableStateOf(AddOrEditPlantMode.NewPlant())

    val existingPlant: MutableStateFlow<Plant?> = MutableStateFlow(null)
    val plantImage: MutableStateFlow<BitmapWithUri?> = MutableStateFlow(null)

    fun saveNewPlant(
        context: Context,
        name: String,
        species: String,
        description: String,
        measurementValueLimits: List<MeasurementValueLimitInEdit>?
    ){
        uiState.value = AddOrEditPlantUiState.SavingChanges()
        val userId = userSessionManager.getUserId()
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
                is DataResult.Success -> {
                    plantImage.value?.let {
                        async {
                            savePlantImage(
                                context = context,
                                plantId = result.data.id,
                            )
                        }.await()
                    }
                    uiState.value = AddOrEditPlantUiState.PlantSaved()
                }
                is DataResult.Exception -> {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
                is DataResult.Error -> {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                }
            }
        }
    }

    fun updateExistingPlant(
        context: Context,
        name: String,
        species: String,
        description: String,
        measurementValueLimits: List<MeasurementValueLimitInEdit>?,
    ){
        uiState.value = AddOrEditPlantUiState.SavingChanges()

        if (existingPlant.value == null){
            uiState.value = AddOrEditPlantUiState.Error(R.string.somethingWentWrong)
        }

        existingPlant.value?.let {
            val plantId = it.id
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
            viewModelScope.launch {
                val result = plantsRepository.updatePlant(updatedPlant)

                when (result) {
                    is DataResult.Success -> {
                        if (plantImage.value != null) {
                            async {
                                savePlantImage(
                                    context = context,
                                    plantId = result.data.id,
                                )
                            }.await()
                        }
                        uiState.value = AddOrEditPlantUiState.PlantSaved()
                    }
                    is DataResult.Exception -> {
                        uiState.value =
                            AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                    }
                    is DataResult.Error -> {
                        uiState.value =
                            AddOrEditPlantUiState.PlantPostFailed(R.string.couldNotSavePlant)
                    }
                }
            }
        }
    }

    fun deleteExistingPlant(){
        existingPlant.value?.let {
            viewModelScope.launch {
                val result = plantsRepository.deletePlant(plantId = it.id)
                if (result is DataResult.Success){
                    uiState.value = AddOrEditPlantUiState.PlantDeleted()
                }
                else {
                    uiState.value = AddOrEditPlantUiState.PlantPostFailed(R.string.failedToDeletePlant)
                }
            }
        }
    }

    private suspend fun savePlantImage(context: Context, plantId: Long){
        val contentResolver = context.contentResolver
        try {
            val plantImageCopy = plantImage.value
            if (plantImageCopy?.uri != null) {
                val stream = contentResolver.openInputStream(plantImageCopy.uri)
                stream?.let {
                    val imageName = ImageUtils.getFileName(context, plantImageCopy.uri)
                    val mediaType = contentResolver.getType(plantImageCopy.uri)?.toMediaTypeOrNull()
                    val bitmap = plantImageCopy.bitmap
                    val imageInBytes = ImageUtils.fromBitmapToByteArray(bitmap)

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
        } catch(ex: java. lang . Exception){
                ex.printStackTrace()
        }
    }

    fun fetchPlant(plantId: Long){
        viewModelScope.launch {
            val plantCall = plantsRepository.getPlantById(plantId)

            when(plantCall){
                is DataResult.Success -> {
                    val resultPlant = plantCall.data
                    if (resultPlant.hasTitleImage){
                        fetchPlantImage(resultPlant)
                        resultPlant.titleImageBitmap = plantImage.value?.bitmap
                    }
                    existingPlant.value = resultPlant
                    mode.value = AddOrEditPlantMode.EditPlant()
                    uiState.value = AddOrEditPlantUiState.PlantToEditLoaded()
                }
                is DataResult.Error -> {
                    uiState.value = AddOrEditPlantUiState.Error(R.string.somethingWentWrong)
                }
                is DataResult.Exception -> {
                    uiState.value = AddOrEditPlantUiState.Error(R.string.connectionError)
                }
            }
        }
    }

    private suspend fun fetchPlantImage(plant: Plant){
        val result = plantsRepository.getPlantImage(
            plantId = plant.id,
            imageQuality = ImageQuality.LARGE
        )
        if (result is DataResult.Success){
            plantImage.value = BitmapWithUri(
                uri = null,
                bitmap = result.data
            )
        }
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