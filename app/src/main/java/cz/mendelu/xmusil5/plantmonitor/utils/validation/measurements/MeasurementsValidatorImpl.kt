package cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements

import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant

class MeasurementsValidatorImpl: IMeasurementsValidator {

    // Percentage of deviation from original measurement, when the measurement will be judged as MINOR INVALID.
    val minorInvalidPercentage = 0.1

    override fun isMeasurementValid(measurement: Measurement, plant: Plant): MeasurementLimitValidation {
        var result = MeasurementLimitValidation.VALID
        for (measurementValue in measurement.values) {
            validateMeasurementValue(
                value = measurementValue.value,
                type = measurementValue.measurementType,
                plant = plant
            ).let {
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
        value: Double,
        type: MeasurementType,
        plant: Plant
    ): MeasurementLimitValidation {
        val minorInvalidPercentage = 0.1

        val suitableLimit = plant.valueLimits.firstOrNull{
            it.type == type
        }
        suitableLimit?.let {
            val range = it.upperLimit - it.lowerLimit
            val minorInvalidRange = range * 0.1
            val minorInvalidLow = it.lowerLimit - minorInvalidRange
            val minorInvalidHigh = it.upperLimit + minorInvalidRange
            when {
                // Measurement is completely valid - between set limits
                it.lowerLimit <= value && it.upperLimit >= value -> {
                    return MeasurementLimitValidation.VALID
                }
                minorInvalidLow <= value && minorInvalidHigh >= value -> {
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