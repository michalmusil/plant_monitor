package cz.mendelu.xmusil5.plantmonitor.models.database.helpers

import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import java.util.Calendar

data class DbLatestMeasurementPlantValue(
    val measurementType: MeasurementType,
    val value: Double,
    val measurementId: Long,
    val datetime: Calendar,
)
