package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.measurements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.MeasurementListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantListItemExpandable
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoPlantMeasurements
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.MeasurementsDateFilter
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailViewModel
import cz.mendelu.xmusil5.plantmonitor.ui.utils.Edges
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges
import java.util.*

@Composable
fun PlantDetailMeasurements(
    viewModel: PlantDetailViewModel
){
    val plant = viewModel.plant.collectAsState()
    val measurements = viewModel.measurements.collectAsState()

    val measurementsToDisplay = remember{
        mutableStateListOf<GetMeasurement>()
    }
    LaunchedEffect(measurements, measurements.value?.size){
        measurements.value?.let {
            measurementsToDisplay.clear()
            val ordered = viewModel.getMeasurementsOrderedForDisplay(it)
            measurementsToDisplay.addAll(ordered)
        }
    }

    plant.value?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MeasurementsDateFilter(
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (measurementsToDisplay.isEmpty()) {
                NoPlantMeasurements(modifier = Modifier.fillMaxWidth())
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .fadeEdges(
                            edges = Edges.VERTICAL,
                            backgroundColor = MaterialTheme.colorScheme.background,
                            fadeWidth = UiConstants.EDGE_FADE_MEDIUM
                        )
                ) {
                    items(
                        count = measurementsToDisplay.count(),
                        key = {
                            measurementsToDisplay[it].id
                        },
                        itemContent = { index ->
                            val measurement = measurementsToDisplay[index]

                            MeasurementListItem(
                                plant = it,
                                measurement = measurement,
                                measurementValidator = viewModel.measurementsValidator
                            )
                        }
                    )
                }
            }
        }
    }
}