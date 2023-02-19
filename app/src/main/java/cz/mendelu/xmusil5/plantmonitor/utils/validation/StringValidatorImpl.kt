package cz.mendelu.xmusil5.plantmonitor.utils.validation

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
}