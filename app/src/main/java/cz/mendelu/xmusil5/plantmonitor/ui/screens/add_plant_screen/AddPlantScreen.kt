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
            is AddPlantUiState.Start -> TODO()
            is AddPlantUiState.PlantSaved -> TODO()
            is AddPlantUiState.PlantPostFailed -> TODO()
            is AddPlantUiState.Error -> TODO()
        }
    }
}

@Composable
fun AddPlantScreenContent(
    viewModel: AddPlantViewModel,
    navigation: INavigationRouter,
    error: MutableState<String>
){
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
        mutableStateOf(true)
    }
    val speciesError = rememberSaveable{
        mutableStateOf(true)
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

        AddPlantForm(
            name = name,
            species = species,
            description = description,
            nameError = nameError,
            speciesError = speciesError,
            viewModel = viewModel
        )
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
    ) {
        if (selectedImage.value != null) {
            Image(
                bitmap = selectedImage.value!!.asImageBitmap(),
                contentDescription = stringResource(id = R.string.plantImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
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
    val cornerRadius = 30.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (-25).dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
            .background(MaterialTheme.colorScheme.background)
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
        
    }
}
