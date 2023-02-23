package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.charts.ChartValueSet

@Composable
fun MeasurementsLineChart(
    chartValues: ChartValueSet,
    selectedData: MutableState<Pair<String, DataPoint>?>,
    showSelected: MutableState<Boolean>,
    height: Dp = 400.dp,
    modifier: Modifier = Modifier
){
    val spaceAroundChart = remember {
        mutableStateOf(40.dp)
    }

    val measurementType = remember{
        mutableStateOf(chartValues.measurementType)
    }
    LaunchedEffect(chartValues){
        measurementType.value = chartValues.measurementType
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        LineGraph(
            plot = LinePlot(
                lines = listOf(
                    LinePlot.Line(
                        chartValues.set,
                        LinePlot.Connection(measurementType.value.color, 2.dp),
                        LinePlot.Intersection(
                            draw = { center, dataPoint ->
                                drawCircle(
                                    color = measurementType.value.color,
                                    radius = 5.dp.toPx(),
                                    center = center
                                )
                            }
                        ),
                        LinePlot.Highlight(androidx.compose.material3.MaterialTheme.colorScheme.secondary, 8.dp),
                        LinePlot.AreaUnderLine(measurementType.value.color, 0.2f)
                    )
                ),
                selection = LinePlot.Selection(
                    highlight = LinePlot.Connection(
                        androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
                    ),
                    detectionTime = 50L
                ),
                horizontalExtraSpace = spaceAroundChart.value,
                xAxis = LinePlot.XAxis(
                    steps = chartValues.labels.size,
                    unit = 0.15f,
                    content = { min, offset, max ->
                        for (it in 0 until chartValues.labels.size) {
                            val value = it * offset + min
                            if (it == 0 || it == chartValues.labels.size-1) {
                                Text(
                                    text = chartValues.labels.getOrNull(it) ?: "?",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                                )
                            }
                            else{
                                Text("")
                            }
                            if (value > max) {
                                break
                            }
                        }
                    }
                )
            ),
            modifier = Modifier
                .fillMaxSize(),
            onSelectionStart = { showSelected.value = true },
            onSelectionEnd = { showSelected.value = false },
            onSelection = { xOffset, datapoints ->
                val index = datapoints.first().x.toInt()
                selectedData.value = Pair(
                    first = chartValues.labels[index],
                    second = chartValues.set[index]
                )
            },
        )
    }
}