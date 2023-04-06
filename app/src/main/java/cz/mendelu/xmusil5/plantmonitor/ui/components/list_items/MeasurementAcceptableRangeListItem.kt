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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import kotlin.math.roundToInt

@Composable
fun MeasurementAcceptableRangeListItem(
    measurementLimit: MeasurementValueLimit
){
    val roundedLower = remember{
        mutableStateOf((measurementLimit.lowerLimit * 10.0).roundToInt() / 10.0)
    }
    val roundedUpper = remember{
        mutableStateOf((measurementLimit.upperLimit * 10.0).roundToInt() / 10.0)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 3.dp, horizontal = 10.dp)
            .clip(CircleShape)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = measurementLimit.type.iconId),
                    contentDescription = stringResource(id = R.string.expand),
                    tint = measurementLimit.type.color,
                    modifier = Modifier
                        .height(35.dp)
                        .aspectRatio(1f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(7f)
            ) {
                Text(
                    text = "${roundedLower.value}${measurementLimit.type.unit}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_range),
                    contentDescription = stringResource(id = R.string.range),
                    tint = measurementLimit.type.color,
                    modifier = Modifier
                        .weight(3f)
                )

                Text(
                    text = "${roundedUpper.value}${measurementLimit.type.unit}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
            }
        }
    }
}