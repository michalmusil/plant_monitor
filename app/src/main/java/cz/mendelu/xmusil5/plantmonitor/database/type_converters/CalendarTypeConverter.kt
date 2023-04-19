package cz.mendelu.xmusil5.plantmonitor.database.type_converters

import androidx.room.TypeConverter
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import java.util.Calendar

class CalendarTypeConverter {

    @TypeConverter
    fun saveCalendarList(calendar: Calendar?): Long? {
        calendar?.let {
            return it.timeInMillis
        }
        return null
    }

    @TypeConverter
    fun getCalendarFromDb(calendarInDb: Long?): Calendar? {
        calendarInDb?.let {
            val output = DateUtils.getCurrentCalendarInUTC0()
            output.timeInMillis = calendarInDb
            return output
        }
        return null
    }

}