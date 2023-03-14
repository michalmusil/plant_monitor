package cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

interface IMeasurementsValidator {
    fun isMeasurementValid(measurement: GetMeasurement, plant: GetPlant): MeasurementLimitValidation

    fun validateMeasurementValue(measurementValue: MeasurementValue, plant: GetPlant): MeasurementLimitValidation
}