package cz.mendelu.xmusil5.plantmonitor.models.api.utils

import java.util.Calendar

data class DateTimeFromApi(
    val originalString: String,
    val calendarInUTC0: Calendar
)
