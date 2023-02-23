package cz.mendelu.xmusil5.plantmonitor.communication.jsonAdapters.utils

import com.squareup.moshi.*
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils

class DateTimeFromApiAdapter: JsonAdapter<DateTimeFromApi>() {
    @FromJson
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

    @ToJson
    override fun toJson(writer: JsonWriter, value: DateTimeFromApi?) {
        writer.value(value?.originalString)
    }
}