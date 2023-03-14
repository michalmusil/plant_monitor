package cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

class MeasurementsValidatorImpl: IMeasurementsValidator {

    // Percentage of deviation from original measurement, when the measurement will be judged as MINOR INVALID.
    val minorInvalidPercentage = 0.1

    override fun isMeasurementValid(measurement: GetMeasurement, plant: GetPlant): MeasurementLimitValidation {
        var result = MeasurementLimitValidation.VALID
        for (measurementValue in measurement.values) {
            validateMeasurementValue(measurementValue = measurementValue, plant = plant).let {
                when(it){
                    MeasurementLimitValidation.MINOR_INVALID -> {
                        result = MeasurementLimitValidation.MINOR_INVALID
                    }
                    MeasurementLimitValidation.INVALID -> {
                        result = MeasurementLimitValidation.INVALID
                        return result
                    }
                }
            }
        }
        return result
    }

    override fun validateMeasurementValue(
        measurementValue: MeasurementValue,
        plant: GetPlant
    ): MeasurementLimitValidation {
        val minorInvalidPercentage = 0.1

        val suitableLimit = plant.valueLimits.firstOrNull{
            it.type == measurementValue.measurementType
        }
        suitableLimit?.let {
            val range = it.upperLimit - it.lowerLimit
            val minorInvalidRange = range * 0.1
            val minorInvalidLow = it.lowerLimit - minorInvalidRange
            val minorInvalidHigh = it.upperLimit + minorInvalidRange
            when {
                // Measurement is completely valid - between set limits
                it.lowerLimit <= measurementValue.value && it.upperLimit >= measurementValue.value -> {
                    return MeasurementLimitValidation.VALID
                }
                minorInvalidLow <= measurementValue.value && minorInvalidHigh >= measurementValue.value -> {
                    return MeasurementLimitValidation.MINOR_INVALID
                }
                else -> {
                    return MeasurementLimitValidation.INVALID
                }
            }
        }
        // If there is no limitation for the measureent type, measurement is valid either way
        return MeasurementLimitValidation.VALID
    }
}