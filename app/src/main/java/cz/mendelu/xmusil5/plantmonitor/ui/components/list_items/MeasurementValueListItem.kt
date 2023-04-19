package cz.mendelu.xmusil5.plantmonitor.ui.components.list_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import kotlin.math.roundToInt

@Composable
fun MeasurementValueListItem(
    measurementValue: MeasurementValue,
    validation: MeasurementLimitValidation?
){
    val valueTextColor = MaterialTheme.colorScheme.onSurface
    val defaultValueBackgroundColor = Color.Transparent

    val roundedValue = remember{
        mutableStateOf((measurementValue.value * 10.0).roundToInt() / 10.0)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .clip(CircleShape)
            .background(
                validation?.color ?: defaultValueBackgroundColor
            )
            .padding(horizontal = 15.dp, vertical = 5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = measurementValue.measurementType.iconId),
                contentDescription = stringResource(id = R.string.expand),
                tint = measurementValue.measurementType.color,
                modifier = Modifier
                    .height(40.dp)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = roundedValue.value.toString() + " ${measurementValue.measurementType.unit}",
                style = MaterialTheme.typography.titleMedium,
                color = valueTextColor
            )
        }
    }
}
