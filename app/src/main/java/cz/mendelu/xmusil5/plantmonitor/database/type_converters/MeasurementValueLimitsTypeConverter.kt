package cz.mendelu.xmusil5.plantmonitor.database.type_converters

import androidx.room.TypeConverter
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbMeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

class MeasurementValueLimitsTypeConverter {
    val listDelimiter = ','

    @TypeConverter
    fun saveMeasurementValueLimitsList(valueLimits: List<DbMeasurementValueLimit>): String {
        var output = ""
        for (i in 0 until valueLimits.size){
            val value = valueLimits.get(i)
            val append = "(${value.type.typeNumber}|${value.lowerLimit};${value.upperLimit})"
            output += append
            if (i < valueLimits.size-1){
                output += listDelimiter
            }
        }
        return output
    }

    @TypeConverter
    fun getMeasurementValueLimitsListFromDb(measurementValueLimitsInDb: String): List<DbMeasurementValueLimit> {
        val measurementValueLimits = mutableListOf<DbMeasurementValueLimit>()
        val stringValues = measurementValueLimitsInDb.split(listDelimiter)
        for (stringValue in stringValues){
            val measurementType = stringValue.substring(
                startIndex = stringValue.indexOf("(")+1,
                endIndex = stringValue.indexOf("|")
            ).toIntOrNull()?.let {
                MeasurementType.getByTypeNumber(it)
            }
            val lowerLimit = stringValue.substring(
                startIndex = stringValue.indexOf("|")+1,
                endIndex = stringValue.indexOf(";")
            ).toDoubleOrNull()
            val upperLimit = stringValue.substring(
                startIndex = stringValue.indexOf(";")+1,
                endIndex = stringValue.indexOf(")")
            ).toDoubleOrNull()
            if (measurementType != null && lowerLimit != null && upperLimit != null){
                val appendedValue = DbMeasurementValueLimit(
                    type = measurementType,
                    lowerLimit = lowerLimit,
                    upperLimit = upperLimit
                )
                measurementValueLimits.add(appendedValue)
            }
        }
        return measurementValueLimits
    }
}