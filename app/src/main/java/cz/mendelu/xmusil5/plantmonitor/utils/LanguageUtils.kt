package cz.mendelu.xmusil5.plantmonitor.utils

import java.text.DecimalFormatSymbols
import java.util.*

object LanguageUtils {
    private val CZECH = "cs"
    private val SLOVAK = "sk"

    fun isLanguageCzech(): Boolean {
        val language = Locale.getDefault().language
        return language.equals(CZECH) || language.equals(SLOVAK)
    }

    fun getDecimalFormatSymbols(): DecimalFormatSymbols {
        return DecimalFormatSymbols(Locale.US)
    }
}