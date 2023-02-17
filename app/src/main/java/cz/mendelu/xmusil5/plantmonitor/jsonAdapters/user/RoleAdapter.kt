package cz.mendelu.xmusil5.plantmonitor.jsonAdapters.user

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role

class RoleAdapter: JsonAdapter<Role>() {
    override fun fromJson(reader: JsonReader): Role? {
        if (reader.peek() != JsonReader.Token.NULL){
            return Role.getByRoleNumber(reader.nextInt()) ?: Role.USER
        }
        return reader.nextNull()
    }

    override fun toJson(writer: JsonWriter, value: Role?) {
        writer.value(value?.roleNumber)
    }
}