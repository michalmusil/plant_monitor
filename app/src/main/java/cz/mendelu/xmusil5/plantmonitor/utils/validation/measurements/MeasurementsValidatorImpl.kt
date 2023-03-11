package cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

class MeasurementsValidatorImpl: IMeasurementsValidator {
    override fun isMeasurementValid(measurement: GetMeasurement, plant: GetPlant): Boolean {
        measurement.values.forEach {
            if (!isMeasurementValueValid(measurementValue = it, plant = plant)){
                return false
            }
        }
        return true
    }

    override fun isMeasurementValueValid(
        measurementValue: MeasurementValue,
        plant: GetPlant
    ): Boolean {
        val suitableLimit = plant.valueLimits.firstOrNull{
            it.type == measurementValue.measurementType
        }
        suitableLimit?.let {
            return it.lowerLimit <= measurementValue.value && it.upperLimit >= measurementValue.value
        }
        return true
    }
}