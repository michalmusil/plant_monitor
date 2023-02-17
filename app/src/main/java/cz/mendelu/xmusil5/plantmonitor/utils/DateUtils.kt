package cz.mendelu.xmusil5.plantmonitor.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    val DATETIME_FORMAT_CS = "dd.MM.yyyy | HH:mm"
    val DATE_FORMAT_CS = "dd.MM.yyyy"
    val TIME_FORMAT_CS = "HH:mm"

    val DATETIME_FORMAT_EN = "yyyy/MM/dd | hh:mmaa"
    val DATE_FORMAT_EN = "yyyy/MM/dd"
    val TIME_FORMAT_EN = "hh:mmaa"

    val DATE_FORMAT_API_GET = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    private val DAYS_OF_WEEK_CS: HashMap<Int, String>
            = hashMapOf(1 to "neděle", 2 to "pondělí", 3 to "úterý", 4 to "středa", 5 to "čtrvrek", 6 to "pátek", 7 to "sobota")
    private val DAYS_OF_WEEK_EN: HashMap<Int, String>
            = hashMapOf(1 to "Sunday", 2 to "Monday", 3 to "Tuesday", 4 to "Wednesday", 5 to "Thursday", 6 to "Friday", 7 to "Saturday")


    fun getCurrentCalendarInUTC0(): Calendar {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    }

    fun calendarFromAPIDateString(apiDate: String): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        val sdf = SimpleDateFormat(DATE_FORMAT_API_GET)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        try {
            calendar.time = sdf.parse(apiDate)!!
        } catch (ex: java.lang.Exception){
            ex.printStackTrace()
        }
        return calendar
    }

    fun apiDateStringFromCalendar(calendar: Calendar): String{
        val calendarAdjusted = calendar.apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val sdf = SimpleDateFormat(DATE_FORMAT_API_GET)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(calendarAdjusted.time)
    }




    fun getLocalizedDateTimeString(calendar: Calendar): String{
        calendar.timeZone = TimeZone.getDefault()
        if (LanguageUtils.isLanguageCzech()){
            return SimpleDateFormat(DATETIME_FORMAT_CS).format(calendar.time)
        } else {
            return SimpleDateFormat(DATETIME_FORMAT_EN).format(calendar.time)
        }
    }

    fun getLocalizedDateString(calendar: Calendar): String{
        calendar.timeZone = TimeZone.getDefault()
        if (LanguageUtils.isLanguageCzech()){
            return SimpleDateFormat(DATE_FORMAT_CS).format(calendar.time)
        } else {
            return SimpleDateFormat(DATE_FORMAT_EN).format(calendar.time)
        }
    }

    fun getLocalizedTimeString(calendar: Calendar): String{
        calendar.timeZone = TimeZone.getDefault()
        if (LanguageUtils.isLanguageCzech()){
            return SimpleDateFormat(TIME_FORMAT_CS).format(calendar.time)
        } else {
            return SimpleDateFormat(TIME_FORMAT_EN).format(calendar.time)
        }
    }

    fun getCalendarWithSubtractedElements(
        original: Calendar,
        days: Int = 0,
        months: Int = 0,
        years: Int = 0
    ): Calendar {
        val subtracted = original
        subtracted.add(Calendar.DATE, -days)
        subtracted.add(Calendar.MONTH, -months)
        subtracted.add(Calendar.YEAR, -years)
        return subtracted
    }


    fun getCurrentUnixTime(): Long{
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }


    fun getDayOfTheWeekString(dateTime: Calendar): String{
        val day = dateTime.get(Calendar.DAY_OF_WEEK)
        val map = if (LanguageUtils.isLanguageCzech()) DAYS_OF_WEEK_CS else DAYS_OF_WEEK_EN
        val dayString = map.get(day)
        return dayString!!
    }

    fun daysBetween(startDate: Calendar, endDate: Calendar): Long {
        val end = endDate.timeInMillis
        val start = startDate.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(end - start)
    }
}