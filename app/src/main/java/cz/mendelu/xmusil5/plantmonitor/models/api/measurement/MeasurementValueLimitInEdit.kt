package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

data class MeasurementValueLimitInEdit(
    var enabled: Boolean,
    var limit: MeasurementValueLimit
)
