package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.madrapps.plot.line.DataPoint
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.charts.ChartValueSet
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MeasurementChartValuePopup
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MeasurementsLineChart
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoPlantMeasurements
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.MeasurementsDateFilter
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailViewModel
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PlantDetailCharts(
    measurements: List<GetMeasurement>,
    from: MutableState<Calendar>,
    to: MutableState<Calendar>,
    viewModel: PlantDetailViewModel
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        
        Spacer(modifier = Modifier.height(20.dp))

        MeasurementsDateFilter(
            from = from,
            to = to
        )

        Spacer(modifier = Modifier.height(20.dp))
        if (measurements.isNotEmpty()) {
            MeasurementsTabView(
                measurements = measurements,
                viewModel = viewModel
            )
        } else {
            Spacer(modifier = Modifier.height(40.dp))

            NoPlantMeasurements(modifier = Modifier.fillMaxWidth())
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

                MeasurementsChart(
                    chartValueSet = chartValueSet
                )
            }
        }
    }
}

@Composable
fun MeasurementsChart(
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

