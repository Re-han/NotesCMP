package domain.core

import platform.Foundation.NSUserDefaults

actual class Preferences {

    actual fun putInt(key: String, value: Int) {
        NSUserDefaults.standardUserDefaults.setInteger(value.toLong(), key)
    }

    actual fun getInt(key: String, default: Int): Int {
        return NSUserDefaults.standardUserDefaults.integerForKey(key).toInt()
    }

    actual fun putString(key: String, value: String) {
        NSUserDefaults.standardUserDefaults.setObject(value, key)
    }

    actual fun getString(key: String): String? {
        return NSUserDefaults.standardUserDefaults.stringForKey(key)
    }

    actual fun putBool(key: String, value: Boolean) {
        NSUserDefaults.standardUserDefaults.setBool(value, key)
    }

    actual fun getBool(key: String, default: Boolean): Boolean {
        return NSUserDefaults.standardUserDefaults.boolForKey(key)
    }

}