package vn.vistark.locas.core

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class Constants {
    companion object {
        private val rangeKey = "RANGE_KEY"
        private val keyToken = "USER_TOKEN"
        private val keyUsername = "USERNAME"
        private val keyPassword = "Password"
        private val keyAvatar = "AVATAR"
        private val keySurename = "SURNAME"
        private val keyName = "NAME"
        private val keyEmail = "EMAIL"
        private val keyPhone = "PHONE_NUMBER"
        private val keyBirthday = "BIRTH_DAY"
        private var keyIsUseAssistant = "IS_USE_ASSISTANT"

        var sharedPreferences: SharedPreferences? = null

        var range: Float
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getFloat(rangeKey, 10000F)
                } else {
                    return 10000F
                }
            }
            set(value) {
                sharedPreferences?.edit()?.putFloat(rangeKey, value)?.apply()
            }
        var birthDay: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyBirthday, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keyBirthday, token)?.apply()
            }

        var phone: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyPhone, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keyPhone, token)?.apply()
            }

        var email: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyEmail, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keyEmail, token)?.apply()
            }

        var isUseAssistant: Boolean
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getBoolean(keyIsUseAssistant, true)
                } else {
                    return true
                }
            }
            set(value) {
                sharedPreferences?.edit()?.putBoolean(keyIsUseAssistant, value)?.apply()
            }

        fun getDisplayName(): String {
            if (name.isEmpty()) {
                return username
            } else {
                if (surname.isNotEmpty()) {
                    return "$surname $name"
                } else {
                    return name
                }
            }
        }


        var avatar: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyAvatar, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keyAvatar, token)?.apply()
            }

        var surname: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keySurename, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keySurename, token)?.apply()
            }

        var name: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyName, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keyName, token)?.apply()
            }

        var token: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyToken, "") ?: ""
                } else {
                    return ""
                }
            }
            set(token) {
                sharedPreferences?.edit()?.putString(keyToken, token)?.apply()
            }
        var username: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyUsername, "") ?: ""
                } else {
                    return ""
                }
            }
            set(value) {
                sharedPreferences?.edit()?.putString(keyUsername, value)?.apply()
            }
        var password: String
            get() {
                if (sharedPreferences != null) {
                    return sharedPreferences!!.getString(keyPassword, "") ?: ""
                } else {
                    return ""
                }
            }
            set(value) {
                sharedPreferences?.edit()?.putString(keyPassword, value)?.apply()
            }

        fun initialize(app: AppCompatActivity) {
            sharedPreferences = app.getSharedPreferences("Locas", Context.MODE_PRIVATE)
        }

    }
}