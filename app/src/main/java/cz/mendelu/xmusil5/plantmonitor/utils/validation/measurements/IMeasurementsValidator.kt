package cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant

interface IMeasurementsValidator {
    fun isMeasurementValid(measurement: Measurement, plant: Plant): MeasurementLimitValidation

    fun validateMeasurementValue(value: Double, type: MeasurementType, plant: Plant): MeasurementLimitValidation
}