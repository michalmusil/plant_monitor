package cz.mendelu.xmusil5.plantmonitor.jsonAdapters.measurement

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType

class MeasurementTypeAdapter: JsonAdapter<MeasurementType>() {
    @FromJson
    override fun fromJson(reader: JsonReader): MeasurementType {
        if (reader.peek() == JsonReader.Token.NULL){
            return MeasurementType.UNKNOWN
        }
        return MeasurementType.getByTypeNumber(reader.nextInt()) ?: MeasurementType.UNKNOWN
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: MeasurementType?) {
        writer.value(value?.typeNumber)
    }
}