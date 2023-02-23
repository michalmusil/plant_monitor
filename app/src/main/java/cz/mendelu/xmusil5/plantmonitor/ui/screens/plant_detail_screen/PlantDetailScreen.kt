package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.utils.BitmapUtils
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import java.util.Calendar

@Composable
fun PlantDetailScreen(
    plantId: Long,
    navigation: INavigationRouter,
    viewModel: PlantDetailViewModel = hiltViewModel()
){
    val plant = remember{
        mutableStateOf<GetPlant?>(null)
    }
    val measurements = remember{
        mutableStateListOf<GetMeasurement>()
    }

    val from = remember{
        mutableStateOf(
            DateUtils.getCalendarWithSubtractedElements(
                original = DateUtils.getCurrentCalendarInUTC0(),
                years = 1
            )
        )
    }
    val to = remember {
        mutableStateOf(DateUtils.getCurrentCalendarInUTC0())
    }

    viewModel.uiState.value.let {
        when(it){
            is PlantDetailUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(it){
                    viewModel.fetchPlant(plantId)
                }
            }
            is PlantDetailUiState.PlantLoaded -> {
                LaunchedEffect(it){
                    plant.value = it.plant

                    viewModel.fetchPlantMeasurements(
                        plantId = it.plant.id,
                        from = from.value,
                        to = to.value
                    )
                }
                plant.value?.let {
                    PlantDetailScreenContent(
                        plant = plant.value!!,
                        measurements = measurements,
                        from = from,
                        to = to,
                        viewModel = viewModel,
                        navigation = navigation
                    )
                }
            }
            is PlantDetailUiState.MeasurementsLoaded -> {
                LaunchedEffect(it){
                    if (plant.value == null){
                        viewModel.uiState.value = PlantDetailUiState.Error(R.string.somethingWentWrong)
                    }
                }
                plant.value?.let {
                    PlantDetailScreenContent(
                        plant = plant.value!!,
                        measurements = measurements,
                        from = from,
                        to = to,
                        viewModel = viewModel,
                        navigation = navigation
                    )
                }
            }
            is PlantDetailUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode))
            }
        }
    }
}

@Composable
fun PlantDetailScreenContent(
    plant: GetPlant,
    measurements: List<GetMeasurement>,
    from: MutableState<Calendar>,
    to: MutableState<Calendar>,
    viewModel: PlantDetailViewModel,
    navigation: INavigationRouter
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PlantDetailImage(plant = plant)

        PlantDetailInfo(
            plant = plant,
            measurements = measurements,
            from = from,
            to = to,
            viewModel = viewModel,
            navigation = navigation
        )
    }
}

@Composable
fun PlantDetailImage(
    plant: GetPlant
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
    ) {
        if (plant.titleImageBitmap != null) {
            Image(
                bitmap = plant.titleImageBitmap!!.asImageBitmap(),
                contentDescription = plant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ){
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_plant_root),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                )
            }

        }
    }
}

@Composable
fun PlantDetailInfo(
    plant: GetPlant,
    measurements: List<GetMeasurement>,
    from: MutableState<Calendar>,
    to: MutableState<Calendar>,
    viewModel: PlantDetailViewModel,
    navigation: INavigationRouter
){
    val topCornerRadius = 25.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (-25).dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = topCornerRadius, topEnd = topCornerRadius))
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = plant.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}