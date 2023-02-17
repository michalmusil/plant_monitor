package cz.mendelu.xmusil5.plantmonitor.jsonAdapters.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils

class DateTimeFromApiAdapter: JsonAdapter<DateTimeFromApi>() {
    override fun fromJson(reader: JsonReader): DateTimeFromApi? {
        if (reader.peek() != JsonReader.Token.NULL){
            val dateTimeString = reader.nextString()
            val calendar = DateUtils.calendarFromAPIDateString(dateTimeString)
            return DateTimeFromApi(
                originalString = dateTimeString,
                calendarInUTC0 = calendar
            )
        }
        return reader.nextNull()
    }

    override fun toJson(writer: JsonWriter, value: DateTimeFromApi?) {
        writer.value(value?.originalString)
    }
}