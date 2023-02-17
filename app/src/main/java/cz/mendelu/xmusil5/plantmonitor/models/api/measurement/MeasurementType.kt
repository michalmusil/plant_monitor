package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role

enum class MeasurementType(val typeNumber: Int) {
    TEMPERATURE(0),
    LIGHT_INTENSITY(1),
    SOIL_MOISTURE(2),

    UNKNOWN(-1);

    companion object {
        fun getByTypeNumber(typeNumber: Int): MeasurementType?{
            return values().firstOrNull { it.typeNumber == typeNumber }
        }
    }
}