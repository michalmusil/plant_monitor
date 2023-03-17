package cz.mendelu.xmusil5.plantmonitor.utils.validation.strings

interface IStringValidator {

    fun isEmailValid(email: String): Boolean

    fun isPasswordValid(password: String): Boolean

    fun isCommunicationIdentifierValid(communicationIdentifier: String): Boolean

    fun isMacAddressValid(macAddress: String): Boolean
}