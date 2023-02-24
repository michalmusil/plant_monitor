package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import com.icontio.senscare_peresonal_mobile.ui.components.templates.TopBarWithBackButton
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomTextField
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailImage
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailInfo
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailViewModel
import java.util.*

@Composable
fun AddPlantScreen(
    navigation: INavigationRouter,
    viewModel: AddPlantViewModel = hiltViewModel()
){
    val errorString = remember{
        mutableStateOf<String?>(null)
    }

    viewModel.uiState.value.let {
        when(it){
            is AddPlantUiState.Start -> {
                AddPlantScreenContent(
                    viewModel = viewModel,
                    navigation = navigation,
                    error = errorString
                )
            }
            is AddPlantUiState.PlantSaved -> {
                LaunchedEffect(it) {
                    navigation.toPlantsScreen()
                }
            }
            is AddPlantUiState.PlantPostFailed -> {
                errorString.value = stringResource(id = it.reasonStringCode)
            }
            is AddPlantUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode))
            }
        }
    }
}

@Composable
fun AddPlantScreenContent(
    viewModel: AddPlantViewModel,
    navigation: INavigationRouter,
    error: MutableState<String?>
){
    val cornerRadius = 30.dp

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
        mutableStateOf<Bitmap?>(null)
    }

    val nameError = rememberSaveable{
        mutableStateOf(false)
    }
    val speciesError = rememberSaveable{
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopBarWithBackButton(
            topBarTitle = stringResource(id = R.string.addPlantScreen),
            onBackClick = {
                navigation.returnBack()
            }
        )

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
            
            CustomButton(
                text = stringResource(id = R.string.saveNewPlant),
                iconId = R.drawable.ic_house_plant,
                tintIcon = false,
                enabled = name.value.isNotEmpty() && species.value.isNotEmpty(),
                onClick = {
                    if (name.value.isNotEmpty() && species.value.isNotEmpty())
                    viewModel.savePlant(
                        name = name.value,
                        species = species.value,
                        description = if (description.value.isNotEmpty()) description.value else null,
                        measurementValueLimits = if (measurementValueLimits.isNotEmpty()) measurementValueLimits else null,
                        plantImage = selectedImage.value
                    )
                }
            )
        }
    }
}

@Composable
fun NewPlantImage(
    selectedImage: MutableState<Bitmap?>,
    viewModel: AddPlantViewModel
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (selectedImage.value != null) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    bitmap = selectedImage.value!!.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.plantImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                CustomButton(
                    text = stringResource(id = R.string.changePlantImage),
                    iconId = R.drawable.ic_change,
                    onClick = {
                        /*TODO*/
                    }
                )
            }
        }
        else {
            CustomButton(
                text = stringResource(id = R.string.addPlantImage),
                iconId = R.drawable.ic_plus,
                onClick = {
                    /*TODO*/
                }
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
    viewModel: AddPlantViewModel,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
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
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

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
