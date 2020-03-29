package vn.vistark.locas.core

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class Constants {
    companion object {
        private val keyToken = "USER_TOKEN"
        private val keyUsername = "USERNAME"
        private val keyPassword = "Password"
        private val keyAvatar = "AVATAR"
        private val keySurename = "SURNAME"
        private val keyName = "NAME"

        var sharedPreferences: SharedPreferences? = null

        fun getDisplayName(): String {
            if (name.isEmpty()) {
                return username
            } else {
                if (username.isNotEmpty()) {
                    return "$username $name"
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