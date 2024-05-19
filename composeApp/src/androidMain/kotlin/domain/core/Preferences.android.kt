package domain.core

import android.content.Context
import android.content.SharedPreferences


actual class Preferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("LocalPreferences", Context.MODE_PRIVATE)

    actual fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    actual fun getInt(key: String, default: Int): Int {
        return preferences.getInt(key, default)
    }

    actual fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    actual fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    actual fun putBool(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    actual fun getBool(key: String, default: Boolean): Boolean {
        return preferences.getBoolean(key, default)
    }
}