package cz.mendelu.xmusil5.plantmonitor.models.enums

import androidx.compose.ui.graphics.Color
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.invalid
import cz.mendelu.xmusil5.plantmonitor.ui.theme.minorInvalid
import cz.mendelu.xmusil5.plantmonitor.ui.theme.valid

enum class MeasurementLimitValidation(val nameId: Int, val color: Color) {
    VALID(R.string.measurementValid, valid),
    MINOR_INVALID(R.string.measurementMinorInvalid, minorInvalid),
    INVALID(R.string.measurementInvalid, invalid)
}