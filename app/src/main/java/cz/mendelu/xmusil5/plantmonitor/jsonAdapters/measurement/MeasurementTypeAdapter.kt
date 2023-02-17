package cz.mendelu.xmusil5.plantmonitor.jsonAdapters.measurement

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType

class MeasurementTypeAdapter: JsonAdapter<MeasurementType>() {
    override fun fromJson(reader: JsonReader): MeasurementType {
        if (reader.peek() == JsonReader.Token.NULL){
            return MeasurementType.UNKNOWN
        }
        return MeasurementType.getByTypeNumber(reader.nextInt()) ?: MeasurementType.UNKNOWN
    }

    override fun toJson(writer: JsonWriter, value: MeasurementType?) {
        writer.value(value?.typeNumber)
    }
}