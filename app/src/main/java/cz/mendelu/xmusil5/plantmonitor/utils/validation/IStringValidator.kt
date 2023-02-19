package cz.mendelu.xmusil5.plantmonitor.utils.validation

interface IStringValidator {

    fun isEmailValid(email: String): Boolean

    fun isPasswordValid(password: String): Boolean
}