package cz.mendelu.xmusil5.plantmonitor.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object LanguageUtils {
    val DATETIME_FORMAT_CS = "dd.MM.yyyy\nHH:mm"
    val DATE_FORMAT_CS = "dd.MM.yyyy"
    val TIME_FORMAT_CS = "HH:mm"

    val DATETIME_FORMAT_EN = "yyyy/MM/dd\nhh:mmaa"
    val DATE_FORMAT_EN = "yyyy/MM/dd"
    val TIME_FORMAT_EN = "hh:mmaa"

    enum class Language(
        val code: String,
        val originName: String,
        val dateFormat: SimpleDateFormat,
        val timeFormat: SimpleDateFormat,
        val dateTimeFormat: SimpleDateFormat
    ){
        ENGLISH(
            code = "en",
            originName = "🇺🇸 English",
            dateFormat = SimpleDateFormat(DATE_FORMAT_EN),
            timeFormat = SimpleDateFormat(TIME_FORMAT_EN),
            dateTimeFormat = SimpleDateFormat(DATETIME_FORMAT_EN)
        ),
        CZECH(
            code = "cs",
            originName = "🇨🇿 Čeština",
            dateFormat = SimpleDateFormat(DATE_FORMAT_CS),
            timeFormat = SimpleDateFormat(TIME_FORMAT_CS),
            dateTimeFormat = SimpleDateFormat(DATETIME_FORMAT_CS)
        );

        companion object {
            fun getByCode(code: String): Language?{
                return Language.values().filter {
                    it.code == code
                }.firstOrNull()
            }

            fun getByCodeDefaultEnglish(code: String): Language{
                return Language.values().filter {
                    it.code == code
                }.firstOrNull() ?: ENGLISH
            }
        }
    }
    /*
    fun updateAppLanguageInUi(context: Context, language: Language){
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
     */
}
