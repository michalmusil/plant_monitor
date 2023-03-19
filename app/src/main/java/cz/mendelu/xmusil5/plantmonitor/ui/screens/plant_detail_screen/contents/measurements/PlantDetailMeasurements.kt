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
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges
import java.util.*

@Composable
fun PlantDetailMeasurements(
    plant: GetPlant,
    measurements: List<GetMeasurement>,
    from: MutableState<Calendar>,
    to: MutableState<Calendar>,
    viewModel: PlantDetailViewModel
){
    val measurementsToDisplay = remember{
        mutableStateListOf<GetMeasurement>()
    }
    LaunchedEffect(measurements, measurements.size){
        measurementsToDisplay.clear()
        val ordered = viewModel.getMeasurementsOrderedForDisplay(measurements)
        measurementsToDisplay.addAll(ordered)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        MeasurementsDateFilter(
            from = from,
            to = to
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (measurementsToDisplay.isEmpty()){
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
                        fadeWidth = 100f
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
                            plant = plant,
                            measurement = measurement,
                            measurementValidator = viewModel.measurementsValidator
                        )
                    }
                )
            }
        }
    }
}