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
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
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

    val mostRecentValues = remember{
        mutableStateOf<List<MeasurementValue>>(listOf())
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
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = plant.species,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (plant.description != null && plant.description.length > 0) {
            PlantDetailDescription(plant = plant)
        }

        plant.associatedDevice?.let {
            DeviceCard(
                device = it,
                onClick = {
                    navigation.toDeviceDetailAndControl(it.id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .customShadow(
                        color = shadowColor,
                        borderRadius = cornerRadius,
                        spread = 0.dp,
                        blurRadius = 5.dp,
                        offsetY = 2.dp
                    )
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(MaterialTheme.colorScheme.surface)
            )
        }

        if (mostRecentValues.value.isNotEmpty()){

            Spacer(modifier = Modifier.height(15.dp))

            DelayedAnimatedAppear {
                MostRecentMeasurementValuesCard(
                    mostRecentValues = mostRecentValues.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(vertical = 25.dp)
                .clip(CircleShape)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.surface)
        )

        Text(
            text = stringResource(id = R.string.measurementValues),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        MeasurementsDateFilter(
            from = from,
            to = to
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (measurements.isNotEmpty()){
            MeasurementsTabView(
                measurements = measurements,
                viewModel = viewModel
            )
        }
        else {
            Spacer(modifier = Modifier.height(40.dp))

            NoPlantMeasurements(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun PlantDetailDescription(
    plant: GetPlant
){
    val cornerRadius = 30.dp
    ExpandableCard(
        headlineContent = {
            Text(
                text = stringResource(id = R.string.plantDescription)
            )
        },
        expandedContent = {
            Text(
                text = plant.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
    )
}

@Composable
fun MeasurementsDateFilter(
    from: MutableState<Calendar>,
    to: MutableState<Calendar>
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.from),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            DatePicker(
                date = from.value,
                onDatePicked = {
                    from.value = it
                }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.to),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            DatePicker(
                date = to.value,
                onDatePicked = {
                    // Needs to be adjusted to include picked day
                    val toInclusive = it.apply {
                        set(Calendar.HOUR, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 999)
                    }
                    to.value = toInclusive
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MeasurementsTabView(
    measurements: List<GetMeasurement>,
    viewModel: PlantDetailViewModel
){
    val measurementTypes = remember{
        mutableStateListOf(
            MeasurementType.TEMPERATURE,
            MeasurementType.LIGHT_INTENSITY,
            MeasurementType.SOIL_MOISTURE
        )
    }
    val selectedMeasurementType = remember{
        mutableStateOf(measurementTypes.first())
    }
    val chartValueSet = remember{
        mutableStateOf(viewModel.getChartValueSetOfType(selectedMeasurementType.value, measurements))
    }
    LaunchedEffect(selectedMeasurementType.value, measurements.size){
        chartValueSet.value = viewModel.getChartValueSetOfType(selectedMeasurementType.value, measurements)
    }

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val tabRowCornerRadius = 30.dp
    LaunchedEffect(pagerState.currentPage){
        selectedMeasurementType.value = measurementTypes[pagerState.currentPage]
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colorScheme.surface,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                        .size(0.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .background(Color.Transparent)
                .clip(RoundedCornerShape(tabRowCornerRadius))
        ) {
            measurementTypes.forEachIndexed { index, item ->
                val tabColor by animateColorAsState(
                    targetValue = if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    animationSpec = tween(
                        durationMillis = 150
                    )
                )
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(tabRowCornerRadius))
                        .background(tabColor),
                    text = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.iconId), 
                            contentDescription = stringResource(id = item.nameId),
                            tint = item.color,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                )
            }
        }
        HorizontalPager(
            count = measurementTypes.size,
            state = pagerState,
        ) {
            val currentType = measurementTypes[pagerState.currentPage]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = currentType.nameId),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                MeasurementValuesData(
                    chartValueSet = chartValueSet
                )
            }
        }
    }
}

@Composable
fun MeasurementValuesData(
    chartValueSet: MutableState<ChartValueSet>
){
    val showPopup = remember{
        mutableStateOf(false)
    }
    val selectedData = remember{
        mutableStateOf<Pair<String, DataPoint>?>(null)
    }
    val measurementType = remember{
        mutableStateOf(chartValueSet.value.measurementType)
    }
    LaunchedEffect(chartValueSet.value){
        measurementType.value = chartValueSet.value.measurementType
    }
    MeasurementsLineChart(
        chartValues = chartValueSet.value,
        selectedData = selectedData,
        showSelected = showPopup
    )

    MeasurementChartValuePopup(
        measurementType = measurementType,
        dataToShow = selectedData,
        visible = showPopup
    )
}

