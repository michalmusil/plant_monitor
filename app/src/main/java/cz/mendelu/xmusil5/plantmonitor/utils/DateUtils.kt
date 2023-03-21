package cz.mendelu.xmusil5.plantmonitor.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

object DateUtils {


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


    fun getDateString(calendar: Calendar): String{
        val language = LanguageUtils.Language
            .getByCodeDefaultEnglish(Locale.getDefault().language)
        return language.dateFormat.format(calendar.time)
    }

    fun getLocalizedDateTimeString(calendar: Calendar): String{
        val language = LanguageUtils.Language
            .getByCodeDefaultEnglish(Locale.getDefault().language)
        calendar.timeZone = TimeZone.getDefault()
        return language.dateTimeFormat.format(calendar.time)
    }

    fun getLocalizedDateString(calendar: Calendar): String{
        val language = LanguageUtils.Language
            .getByCodeDefaultEnglish(Locale.getDefault().language)
        calendar.timeZone = TimeZone.getDefault()
        return language.dateFormat.format(calendar.time)
    }

    fun getLocalizedTimeString(calendar: Calendar): String{
        val language = LanguageUtils.Language
            .getByCodeDefaultEnglish(Locale.getDefault().language)
        calendar.timeZone = TimeZone.getDefault()
        return language.timeFormat.format(calendar.time)
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

    fun getCalendarFromDateComponents(
        year: Int,
        month: Int,
        day: Int
    ): Calendar{
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("UTC")
        cal.set(year, month, day, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }

    fun getDayOfTheWeekString(dateTime: Calendar): String{
        val languageCode = Locale.getDefault().language
        val day = dateTime.get(Calendar.DAY_OF_WEEK)
        val map = getDaysOfWeekByLanguageCode(languageCode)
        val dayString = map.get(day)
        return dayString!!
    }

    fun daysBetween(startDate: Calendar, endDate: Calendar): Long {
        val end = endDate.timeInMillis
        val start = startDate.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(end - start)
    }

    fun getDaysOfWeekByLanguageCode(languageCode: String): HashMap<Int, String>{
        val language = LanguageUtils.Language.getByCodeDefaultEnglish(languageCode)
        when (language){
            LanguageUtils.Language.CZECH -> {
                return DAYS_OF_WEEK_CS
            }
            LanguageUtils.Language.ENGLISH -> {
                return DAYS_OF_WEEK_EN
            }
        }
    }
}