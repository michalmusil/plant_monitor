package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.DeviceListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.LatestMeasurementValueListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.MeasurementValueListItem
import cz.mendelu.xmusil5.plantmonitor.ui.utils.Edges
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import kotlin.math.roundToInt

@Composable
fun MostRecentMeasurementValuesCard(
    plant: GetPlant,
    mostRecentValues: List<LatestMeasurementValueOfPlant>,
    measurementsValidator: IMeasurementsValidator,
    modifier: Modifier = Modifier
){
    val cornerRadius = UiConstants.RADIUS_LARGE
    val validatedTypeLimits = remember{
        mutableStateListOf<Pair<MeasurementType, MeasurementLimitValidation>>()
    }

    LaunchedEffect(measurementsValidator){
        validatedTypeLimits.clear()
        mostRecentValues.forEach {
            val validation = measurementsValidator.validateMeasurementValue(
                value = it.value,
                type = it.measurementType,
                plant = plant)
            val validatedType = Pair(it.measurementType, validation)
            validatedTypeLimits.add(validatedType)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.latestValues),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyHorizontalGrid(
                rows = GridCells.Adaptive(150.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
            ){
                items(
                    count = mostRecentValues.count(),
                    key = {
                        mostRecentValues[it].hashCode()
                    },
                    itemContent = { index ->
                        LatestMeasurementValueListItem(
                            measurementValue = mostRecentValues[index],
                            measurementValidation = validatedTypeLimits.firstOrNull{
                                it.first == mostRecentValues[index].measurementType
                            }?.second ?: MeasurementLimitValidation.VALID
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
