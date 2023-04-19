package cz.mendelu.xmusil5.plantmonitor.models.database.helpers

import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

data class DbMeasurementValueLimit(
    val type: MeasurementType,
    var lowerLimit: Double,
    var upperLimit: Double,
)
