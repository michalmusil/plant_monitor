package cz.mendelu.xmusil5.plantmonitor.database.type_converters

import androidx.room.TypeConverter
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbMeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType

class MeasurementValuesTypeConverter {
    val listDelimiter = ','

    @TypeConverter
    fun saveMeasurementValuesList(measurementValues: List<DbMeasurementValue>): String {
        var output = ""
        for (i in 0 until measurementValues.size){
            val value = measurementValues.get(i)
            val append = "(${value.measurementType.typeNumber}|${value.value})"
            output += append
            if (i < measurementValues.size-1){
                output += listDelimiter
            }
        }
        return output
    }

    @TypeConverter
    fun getMeasurementValuesListFromDb(measurementValuesInDb: String): List<DbMeasurementValue> {
        val measurementValues = mutableListOf<DbMeasurementValue>()
        val stringValues = measurementValuesInDb.split(listDelimiter)
        for (stringValue in stringValues){
            val measurementType = stringValue.substring(
                startIndex = stringValue.indexOf("(")+1,
                endIndex = stringValue.indexOf("|")
            ).toIntOrNull()?.let {
                MeasurementType.getByTypeNumber(it)
            }
            val value = stringValue.substring(
                startIndex = stringValue.indexOf("|")+1,
                endIndex = stringValue.indexOf(")")
            ).toDoubleOrNull()
            if (measurementType != null && value != null){
                val appendedValue = DbMeasurementValue(
                    measurementType = measurementType,
                    value = value
                )
                measurementValues.add(appendedValue)
            }
        }
        return measurementValues
    }
}