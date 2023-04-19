package cz.mendelu.xmusil5.plantmonitor.models.support

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit

data class MeasurementValueLimitInEdit(
    var enabled: Boolean,
    var limit: MeasurementValueLimit
)
