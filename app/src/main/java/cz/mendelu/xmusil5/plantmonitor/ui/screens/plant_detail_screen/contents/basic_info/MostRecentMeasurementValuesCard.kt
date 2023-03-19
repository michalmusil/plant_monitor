package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

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
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import kotlin.math.roundToInt

@Composable
fun MostRecentMeasurementValuesCard(
    plant: GetPlant,
    mostRecentValues: List<LatestMeasurementValueOfPlant>,
    measurementsValidator: IMeasurementsValidator,
    modifier: Modifier = Modifier
){
    val cornerRadius = 30.dp
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

            mostRecentValues.forEach { measurementValue ->
                LatestValueItem(
                    measurementValue = measurementValue,
                    measurementValidation = validatedTypeLimits.firstOrNull{
                        it.first == measurementValue.measurementType
                    }?.second ?: MeasurementLimitValidation.VALID
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun LatestValueItem(
    measurementValue: LatestMeasurementValueOfPlant,
    measurementValidation: MeasurementLimitValidation,
    textColor: Color = MaterialTheme.colorScheme.onSurface
){
    val roundedValue = remember{
        mutableStateOf((measurementValue.value * 10.0).roundToInt() / 10.0)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 3.dp, horizontal = 10.dp)
            .clip(CircleShape)
            .fillMaxWidth()
            .background(measurementValidation.color)
            .padding(vertical = 10.dp, horizontal = 16.dp)
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

                Text(
                    text = roundedValue.value.toString() + " ${measurementValue.measurementType.unit}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                measurementValue.datetime?.calendarInUTC0?.let {
                    Text(
                        text = DateUtils.getLocalizedDateTimeString(it),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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