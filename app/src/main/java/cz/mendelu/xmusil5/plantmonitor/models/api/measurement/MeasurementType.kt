package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role

enum class MeasurementType(val typeNumber: Int, val unit: String) {
    TEMPERATURE(0, "°C"),
    LIGHT_INTENSITY(1, "lux"),
    SOIL_MOISTURE(2, "%"),

    UNKNOWN(-1, "");

    companion object {
        fun getByTypeNumber(typeNumber: Int): MeasurementType?{
            return values().firstOrNull { it.typeNumber == typeNumber }
        }
    }
}