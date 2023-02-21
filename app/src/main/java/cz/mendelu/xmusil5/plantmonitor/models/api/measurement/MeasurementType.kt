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
    val iconId: Int
    ) {
    TEMPERATURE(
        typeNumber = 0,
        nameId = R.string.temperature,
        unit = "°C",
        color = temperature,
        iconId = R.drawable.ic_thermometer
    ),
    LIGHT_INTENSITY(
        typeNumber = 1,
        nameId = R.string.lightIntensity,
        unit = "lux",
        color = lightIntensity,
        iconId = R.drawable.ic_sun
    ),
    SOIL_MOISTURE(
        typeNumber = 2,
        nameId = R.string.soilMoisture,
        unit = "%",
        color = soilMoisture,
        iconId = R.drawable.ic_water
    ),

    UNKNOWN(
        typeNumber = -1,
        nameId = R.string.unknown,
        unit = "",
        color = disabledColor,
        iconId = R.drawable.ic_questionmark);

    companion object {
        fun getByTypeNumber(typeNumber: Int): MeasurementType?{
            return values().firstOrNull { it.typeNumber == typeNumber }
        }
    }
}