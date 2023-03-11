package cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

interface IMeasurementsValidator {
    fun isMeasurementValid(measurement: GetMeasurement, plant: GetPlant): Boolean

    fun isMeasurementValueValid(measurementValue: MeasurementValue, plant: GetPlant): Boolean
}