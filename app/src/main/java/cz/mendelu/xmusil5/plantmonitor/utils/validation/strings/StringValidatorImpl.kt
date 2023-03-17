package cz.mendelu.xmusil5.plantmonitor.utils.validation.strings

import android.text.TextUtils

class StringValidatorImpl: IStringValidator {
    override fun isEmailValid(email: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    override fun isPasswordValid(password: String): Boolean {
        return password.length >= 7
    }

    override fun isCommunicationIdentifierValid(communicationIdentifier: String): Boolean {
        return communicationIdentifier.length >= 30
    }

    override fun isMacAddressValid(macAddress: String): Boolean {
        val regex = Regex("([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}")
        return macAddress.matches(regex)
    }
}