package cz.mendelu.xmusil5.plantmonitor.ui.components.list_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import kotlin.math.roundToInt

@Composable
fun LatestMeasurementValueListItem(
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
            .padding(8.dp)
            .size(125.dp)
            .clip(RoundedCornerShape(UiConstants.RADIUS_SMALL))
            .background(measurementValidation.color)
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
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
                    text = roundedValue.value.toString() + " ${measurementValue.measurementType.unit}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            Box {
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
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = textColor
            )


        }
    }
}