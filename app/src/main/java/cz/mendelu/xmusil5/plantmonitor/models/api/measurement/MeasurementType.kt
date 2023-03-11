package cz.mendelu.xmusil5.plantmonitor.models.api.measurement

import androidx.compose.ui.graphics.Color
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.user.Role
import cz.mendelu.xmusil5.plantmonitor.ui.theme.disabledColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.lightIntensity
import cz.mendelu.xmusil5.plantmonitor.ui.theme.soilMoisture
import cz.mendelu.xmusil5.plantmonitor.ui.theme.temperature

enum class MeasurementType(
    val typeNumber: Int,
    val nameId: Int,
    val unit: String,
    val color: Color,
    val iconId: Int,
    val minLimit: Double,
    val maxLimit: Double
    ) {
    TEMPERATURE(
        typeNumber = 0,
        nameId = R.string.temperature,
        unit = "°C",
        color = temperature,
        iconId = R.drawable.ic_thermometer,
        minLimit = -30.0,
        maxLimit = 55.0
    ),
    LIGHT_INTENSITY(
        typeNumber = 1,
        nameId = R.string.lightIntensity,
        unit = "lux",
        color = lightIntensity,
        iconId = R.drawable.ic_sun,
        minLimit = 0.0,
        maxLimit = 50000.0
    ),
    SOIL_MOISTURE(
        typeNumber = 2,
        nameId = R.string.soilMoisture,
        unit = "%",
        color = soilMoisture,
        iconId = R.drawable.ic_water,
        minLimit = 0.0,
        maxLimit = 100.0
    ),

    UNKNOWN(
        typeNumber = -1,
        nameId = R.string.unknown,
        unit = "",
        color = disabledColor,
        iconId = R.drawable.ic_questionmark,
        minLimit = 0.0,
        maxLimit = 0.0
    );

    companion object {
        fun getByTypeNumber(typeNumber: Int): MeasurementType?{
            return values().firstOrNull { it.typeNumber == typeNumber }
        }

        fun getValidTypes(): List<MeasurementType>{
            return values().filter {
                it != UNKNOWN
            }
        }
    }
}