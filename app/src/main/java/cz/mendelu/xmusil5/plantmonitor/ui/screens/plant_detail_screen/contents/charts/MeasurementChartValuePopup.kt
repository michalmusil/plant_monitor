package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.madrapps.plot.line.DataPoint
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import kotlin.math.roundToInt

@Composable
fun MeasurementChartValuePopup(
    measurementType: MutableState<MeasurementType>,
    dataToShow: MutableState<Pair<String, DataPoint>?>,
    visible: MutableState<Boolean>
) {
    val showValues = remember {
        mutableStateOf(false)
    }
    val roundedValue = remember {
        mutableStateOf<Double?>(null)
    }
    LaunchedEffect(dataToShow.value) {
        dataToShow.value?.let {
            roundedValue.value = (it.second.y * 100).roundToInt() / 100.0
            showValues.value = true
            return@LaunchedEffect
        }
        showValues.value = false
    }

    val cornerRadius by remember {
        mutableStateOf(8.dp)
    }
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { visible.value = false }
    ) {
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(),
            exit = fadeOut(),

            ) {
            if (showValues.value) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(cornerRadius)
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = measurementType.value.iconId
                                ),
                                contentDescription = stringResource(id = measurementType.value.nameId),
                                tint = measurementType.value.color,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                            Text(
                                text = stringResource(id = measurementType.value.nameId),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${roundedValue.value?.toString() ?: ""} ${measurementType.value.unit}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = dataToShow.value?.first ?: "",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}