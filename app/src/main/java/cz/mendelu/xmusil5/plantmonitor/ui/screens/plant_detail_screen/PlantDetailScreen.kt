package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import com.icontio.senscare_peresonal_mobile.ui.components.templates.DelayedAnimatedAppear
import com.icontio.senscare_peresonal_mobile.ui.components.templates.TopBarWithBackButton
import com.madrapps.plot.line.DataPoint
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.charts.ChartValueSet
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.DeviceCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MeasurementChartValuePopup
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MeasurementsLineChart
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MostRecentMeasurementValuesCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoPlantMeasurements
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.DatePicker
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.ExpandableCard
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.PlantDetailBasicInfo
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.PlantDetailCharts
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.PlantInfoContentMode
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import kotlinx.coroutines.launch
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
    LaunchedEffect(from.value, to.value){
        plant.value?.let {
            viewModel.fetchPlantMeasurements(
                plantId = it.id,
                from = from.value,
                to = to.value
            )
        }
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
                    measurements.clear()
                    measurements.addAll(it.measurements)
                    if (plant.value == null){
                        viewModel.uiState.value = PlantDetailUiState.Start()
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
                ErrorScreen(text = stringResource(id = it.errorStringCode)){
                    viewModel.uiState.value = PlantDetailUiState.Start()
                }
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
        TopBarWithBackButton(
            topBarTitle = stringResource(id = R.string.plantDetailScreen),
            onBackClick = {
                navigation.returnBack()
            },
            actions = {
                IconButton(
                    onClick = {
                        navigation.toEditPlant(plantId = plant.id)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )

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
            .height(350.dp)
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = plant.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            PlantInfoContentTab(
                plant = plant,
                measurements = measurements,
                from = from,
                to = to,
                viewModel = viewModel,
                navigation = navigation
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PlantInfoContentTab(
    plant: GetPlant,
    measurements: List<GetMeasurement>,
    from: MutableState<Calendar>,
    to: MutableState<Calendar>,
    viewModel: PlantDetailViewModel,
    navigation: INavigationRouter
){
    val contentModes = remember{
        mutableStateListOf(
            PlantInfoContentMode.BASIC_INFO,
            PlantInfoContentMode.MEASUREMENTS,
            PlantInfoContentMode.CHARTS,
        )
    }
    val selectedContentMode = remember{
        mutableStateOf(contentModes.first())
    }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(pagerState.currentPage){
        selectedContentMode.value = contentModes[pagerState.currentPage]
    }

    val mostRecentValues = remember{
        mutableStateOf<List<LatestMeasurementValueOfPlant>>(listOf())
    }
    LaunchedEffect(plant, measurements){
        viewModel.fetchMostRecentValuesOfPlant(
            plant = plant,
            onValuesFetched = {
                mostRecentValues.value = it
            }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(pagerState, tabPositions),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            contentModes.forEachIndexed { index, item ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(id = item.nameId),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        HorizontalPager(
            count = contentModes.size,
            state = pagerState,
        ) {
            val currentType = contentModes[pagerState.currentPage]
            when(currentType){
                PlantInfoContentMode.BASIC_INFO -> {
                    PlantDetailBasicInfo(
                        plant = plant,
                        mostRecentValues = mostRecentValues.value,
                        navigation = navigation,
                        viewModel = viewModel
                    )
                }
                PlantInfoContentMode.MEASUREMENTS -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
                PlantInfoContentMode.CHARTS -> {
                    PlantDetailCharts(
                        measurements = measurements,
                        from = from,
                        to = to,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}