package cz.mendelu.xmusil5.plantmonitor.communication.jsonAdapters.user

import com.squareup.moshi.*
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role

class RoleAdapter: JsonAdapter<Role>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Role? {
        if (reader.peek() != JsonReader.Token.NULL){
            return Role.getByRoleNumber(reader.nextInt()) ?: Role.USER
        }
        return reader.nextNull()
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Role?) {
        writer.value(value?.roleNumber)
    }
}