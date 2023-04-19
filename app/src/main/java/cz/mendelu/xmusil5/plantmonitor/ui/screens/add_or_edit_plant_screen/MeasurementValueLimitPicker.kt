package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.ui.theme.disabledColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeasurementValueLimitPicker(
    measurementValueLimit: MutableState<MeasurementValueLimit>,
    limitEnabled: MutableState<Boolean>,
    onLimitPicked: (MeasurementValueLimit) -> Unit,
    onEnabledChanged: (Boolean) -> Unit,
    title: String,
    sliderColor: Color = MaterialTheme.colorScheme.secondary,
    parentScrollState: ScrollState? = null,
    modifier: Modifier = Modifier
){
    val cornerRadius = UiConstants.RADIUS_LARGE
    val coroutineScope = rememberCoroutineScope()

    val iconColor by animateColorAsState(
        targetValue = if (limitEnabled.value) measurementValueLimit.value.type.color else disabledColor,
        animationSpec = tween(
            durationMillis = 100
        )
    )

    val currentLow = remember {
        mutableStateOf(measurementValueLimit.value.lowerLimit)
    }

    val currentHigh = remember {
        mutableStateOf(measurementValueLimit.value.upperLimit)
    }

    LaunchedEffect(limitEnabled.value){
        onEnabledChanged(limitEnabled.value)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ){
        
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = measurementValueLimit.value.type.iconId),
                    contentDescription = stringResource(id = measurementValueLimit.value.type.nameId),
                    tint = iconColor,
                    modifier = Modifier
                        .size(25.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Checkbox(
                    checked = limitEnabled.value,
                    onCheckedChange = {
                        limitEnabled.value = it
                    },
                )
            }
            
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "${currentLow.value} ${measurementValueLimit.value.type.unit}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${currentHigh.value} ${measurementValueLimit.value.type.unit}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            RangeSlider(
                enabled = limitEnabled.value,
                valueRange = measurementValueLimit.value.type.minLimit.toFloat()..measurementValueLimit.value.type.maxLimit.toFloat(),
                value = currentLow.value.toFloat()..currentHigh.value.toFloat(),
                onValueChange = {
                    currentLow.value = it.start.roundToInt().toDouble()
                    currentHigh.value = it.endInclusive.roundToInt().toDouble()
                    parentScrollState?.let {
                        coroutineScope.launch {
                            // scrolling on parent is stopped when editing
                            it.stopScroll(MutatePriority.UserInput)
                        }
                    }
                },
                onValueChangeFinished = {
                    measurementValueLimit.value.lowerLimit = currentLow.value
                    measurementValueLimit.value.upperLimit = currentHigh.value
                    onLimitPicked(measurementValueLimit.value)
                },
                colors = SliderDefaults.colors(
                    thumbColor = sliderColor,
                    activeTrackColor = sliderColor.copy(alpha = 0.6f),
                    inactiveTrackColor = disabledColor.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}