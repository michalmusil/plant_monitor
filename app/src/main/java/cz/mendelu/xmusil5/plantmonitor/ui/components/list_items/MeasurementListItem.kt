package cz.mendelu.xmusil5.plantmonitor.ui.components.list_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import kotlin.math.roundToInt

@Composable
fun MeasurementListItem(
    plant: Plant,
    measurement: Measurement,
    measurementValidator: IMeasurementsValidator? = null,
){
    val cornerRadius = UiConstants.RADIUS_MEDIUM
    val validatedTypeLimits = remember{
        mutableStateListOf<Pair<MeasurementType, MeasurementLimitValidation>>()
    }

    LaunchedEffect(measurementValidator, measurement){
        measurementValidator?.let { validator ->
            validatedTypeLimits.clear()
            measurement.values.forEach {
                val validation = validator.validateMeasurementValue(
                    value = it.value,
                    type = it.measurementType,
                    plant = plant
                )
                val validatedType = Pair(it.measurementType, validation)
                validatedTypeLimits.add(validatedType)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        measurement.datetime?.let {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = DateUtils.getLocalizedDateString(it.calendarInUTC0),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = DateUtils.getLocalizedTimeString(it.calendarInUTC0),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                measurement.values.forEach { measurementValue ->
                    ValueOfMeasurementListItem(
                        measurementValue = measurementValue,
                        measurementValidation = validatedTypeLimits.firstOrNull{
                            it.first == measurementValue.measurementType
                        }?.second ?: MeasurementLimitValidation.VALID
                    )
                }
            }
        }
    }
}

@Composable
fun ValueOfMeasurementListItem(
    measurementValue: MeasurementValue,
    measurementValidation: MeasurementLimitValidation
){
    val textColor = MaterialTheme.colorScheme.onSurface
    val roundedValue = remember{
        mutableStateOf((measurementValue.value * 10.0).roundToInt() / 10.0)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 3.dp)
            .clip(CircleShape)
            .fillMaxWidth()
            .background(measurementValidation.color)
            .padding(5.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(2f)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = measurementValue.measurementType.iconId),
                    contentDescription = stringResource(id = R.string.expand),
                    tint = measurementValue.measurementType.color,
                    modifier = Modifier
                        .height(35.dp)
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = measurementValue.measurementType.nameId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )

                    Text(
                        text = roundedValue.value.toString() + " ${measurementValue.measurementType.unit}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
            Text(
                text = stringResource(id = measurementValidation.nameId),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}
