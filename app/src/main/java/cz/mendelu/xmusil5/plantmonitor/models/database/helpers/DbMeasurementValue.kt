package cz.mendelu.xmusil5.plantmonitor.models.database.helpers

import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

data class DbMeasurementValue(
    val measurementType: MeasurementType,
    val value: Double,
)
