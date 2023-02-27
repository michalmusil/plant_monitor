package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_or_edit_plant_screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import com.icontio.senscare_peresonal_mobile.ui.components.templates.TopBarWithBackButton
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.templates.PopupDialog
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomTextField
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.GalleryLauncherButton

@Composable
fun AddOrEditPlantScreen(
    existingPlantId: Long?,
    navigation: INavigationRouter,
    viewModel: AddOrEditPlantViewModel = hiltViewModel()
){
    val errorString = remember{
        mutableStateOf<String?>(null)
    }

    viewModel.uiState.value.let {
        when(it){
            is AddOrEditPlantUiState.Start -> {
                if (existingPlantId == null){
                    AddPlantScreenContent(
                        viewModel = viewModel,
                        navigation = navigation,
                        error = errorString
                    )
                }
                else {
                    LaunchedEffect(it) {
                        viewModel.fetchPlant(existingPlantId)
                    }
                    LoadingScreen()
                }
            }
            is AddOrEditPlantUiState.PlantToEditLoaded -> {
                AddPlantScreenContent(
                    viewModel = viewModel,
                    navigation = navigation,
                    error = errorString
                )
            }
            is AddOrEditPlantUiState.PlantSaved -> {
                LaunchedEffect(it) {
                    navigation.toPlantsScreen()
                }
            }
            is AddOrEditPlantUiState.PlantDeleted -> {
                LaunchedEffect(it){
                    navigation.toPlantsScreen()
                }
            }
            is AddOrEditPlantUiState.PlantPostFailed -> {
                val error = stringResource(id = it.reasonStringCode)
                LaunchedEffect(it){
                    errorString.value = error
                }
                AddPlantScreenContent(
                    viewModel = viewModel,
                    navigation = navigation,
                    error = errorString
                )
            }
            is AddOrEditPlantUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode))
            }
        }
    }
}

@Composable
fun AddPlantScreenContent(
    viewModel: AddOrEditPlantViewModel,
    navigation: INavigationRouter,
    error: MutableState<String?>
){
    val cornerRadius = 30.dp
    val context = LocalContext.current

    val name = rememberSaveable{
        mutableStateOf("")
    }
    val species = rememberSaveable{
        mutableStateOf("")
    }
    val description = rememberSaveable{
        mutableStateOf("")
    }
    val measurementValueLimits = remember{
        mutableStateListOf<MeasurementValueLimit>()
    }
    val selectedImage = remember {
        mutableStateOf<Pair<Uri, Bitmap>?>(null)
    }

    val nameError = rememberSaveable{
        mutableStateOf(false)
    }
    val speciesError = rememberSaveable{
        mutableStateOf(false)
    }
    val showDeleteDialog = rememberSaveable{
        mutableStateOf(false)
    }

    // When mode is edit, i fill all the plants info out
    viewModel.mode.value.let {
        if (it is AddOrEditPlantMode.EditPlant){
            LaunchedEffect(it){
                name.value = it.plant.name
                species.value = it.plant.species
                description.value = it.plant.description ?: ""
                measurementValueLimits.clear()
                measurementValueLimits.addAll(it.plant.valueLimits)
                it.plant.titleImageBitmap?.let {
                    selectedImage.value = Pair(Uri.EMPTY, it)
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        TopBarWithBackButton(
            topBarTitle = stringResource(
                id = when(viewModel.mode.value){
                    is AddOrEditPlantMode.EditPlant -> R.string.editPlantScreen
                    is AddOrEditPlantMode.NewPlant -> R.string.addPlantScreen
                }
            ),
            actions = {
                if (viewModel.mode.value is AddOrEditPlantMode.EditPlant){
                    IconButton(
                        onClick = {
                            showDeleteDialog.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.edit),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            onBackClick = {
                navigation.returnBack()
            }
        )

        if (showDeleteDialog.value) {
            PopupDialog(
                showDialog = showDeleteDialog,
                title = stringResource(id = R.string.deletePlant),
                text = stringResource(id = R.string.sureToDeletePlant),
                confirmButtonText = stringResource(id = R.string.yes),
                cancelButtonText = stringResource(id = R.string.no),
                onConfirm = {
                    viewModel.mode.value.let {
                        if (it is AddOrEditPlantMode.EditPlant) {
                            viewModel.deletePlant(it.plant)
                        }
                    }
                },
                onCancel = {
                    showDeleteDialog.value = false
                }
            )
        }

        NewPlantImage(
            selectedImage = selectedImage,
            viewModel = viewModel
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset(y = (-25).dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(MaterialTheme.colorScheme.background)
        ) {

            AddPlantForm(
                name = name,
                species = species,
                description = description,
                nameError = nameError,
                speciesError = speciesError,
                viewModel = viewModel
            )

            SavePlantButton(
                name = name,
                species = species,
                description = description,
                selectedImage = selectedImage,
                measurementValueLimits = measurementValueLimits,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun NewPlantImage(
    selectedImage: MutableState<Pair<Uri, Bitmap>?>,
    viewModel: AddOrEditPlantViewModel
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (selectedImage.value != null) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    bitmap = selectedImage.value!!.second.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.plantImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                GalleryLauncherButton(
                    text = stringResource(id = R.string.changePlantImage),
                    iconId = R.drawable.ic_change,
                    onImagePicked = { uri, bitmap ->
                        bitmap?.let {
                            selectedImage.value = Pair(uri, it)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 40.dp)
                )
            }
        }
        else {
            GalleryLauncherButton(
                text = stringResource(id = R.string.addPlantImage),
                iconId = R.drawable.ic_plus,
                onImagePicked = { uri, bitmap ->
                    bitmap?.let {
                        selectedImage.value = Pair(uri, it)
                    }
                },
            )
        }
    }
}

@Composable
fun AddPlantForm(
    name: MutableState<String>,
    species: MutableState<String>,
    description: MutableState<String>,
    nameError: MutableState<Boolean>,
    speciesError: MutableState<Boolean>,
    viewModel: AddOrEditPlantViewModel,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = name.value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = species.value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        CustomTextField(
            labelTitle = stringResource(id = R.string.plantName),
            value = name,
            maxChars = 50,
            isError = nameError.value,
            errorMessage = stringResource(id = R.string.plantNameCantBeEmpty),
            onTextChanged = {
                nameError.value = it.isBlank()
            }
        )
        CustomTextField(
            labelTitle = stringResource(id = R.string.plantSpecies),
            value = species,
            maxChars = 50,
            isError = speciesError.value,
            errorMessage = stringResource(id = R.string.plantSpeciesCantBeEmpty),
            onTextChanged = {
                speciesError.value = it.isBlank()
            }
        )
        CustomTextField(
            labelTitle = stringResource(id = R.string.plantDescription),
            value = description,
            singleLine = false,
        )
        
    }
}

@Composable
fun SavePlantButton(
    name: MutableState<String>,
    species: MutableState<String>,
    description: MutableState<String>,
    selectedImage: MutableState<Pair<Uri, Bitmap>?>,
    measurementValueLimits: List<MeasurementValueLimit>,
    viewModel: AddOrEditPlantViewModel,
){
    val context = LocalContext.current

    CustomButton(
        text = stringResource(
            id = when(viewModel.mode.value){
                is AddOrEditPlantMode.EditPlant -> R.string.saveChanges
                is AddOrEditPlantMode.NewPlant -> R.string.saveNewPlant
            }
        ),
        iconId = R.drawable.ic_house_plant,
        tintIcon = false,
        enabled = name.value.isNotEmpty() && species.value.isNotEmpty(),
        onClick = {
            if (name.value.isNotEmpty() && species.value.isNotEmpty()) {
                viewModel.mode.value.let {
                    when (it) {
                        is AddOrEditPlantMode.NewPlant -> {
                            viewModel.saveNewPlant(
                                context = context,
                                name = name.value,
                                species = species.value,
                                description = if (description.value.isNotEmpty()) description.value else null,
                                measurementValueLimits = if (measurementValueLimits.isNotEmpty()) measurementValueLimits else null,
                                plantImageUri = selectedImage.value?.first
                            )
                        }
                        is AddOrEditPlantMode.EditPlant -> {
                            viewModel.updateExistingPlant(
                                context = context,
                                existingPlant = it.plant,
                                name = name.value,
                                species = species.value,
                                description = if (description.value.isNotEmpty()) description.value else null,
                                measurementValueLimits = if (measurementValueLimits.isNotEmpty()) measurementValueLimits else null,
                                plantImageUri = selectedImage.value?.first
                            )
                        }
                    }
                }
            }
        }
    )
}