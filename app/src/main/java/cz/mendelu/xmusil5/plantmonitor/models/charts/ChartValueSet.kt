package cz.mendelu.xmusil5.plantmonitor.models.charts

import com.madrapps.plot.line.DataPoint
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

data class ChartValueSet(
    val measurementType: MeasurementType,
    val set: List<DataPoint>,
    val labels: List<String>
)