package domain.core

expect class Preferences {

    fun putInt(key: String, value: Int)

    fun getInt(key: String, default: Int): Int

    fun putString(key: String, value: String)

    fun getString(key: String): String?

    fun putBool(key: String, value: Boolean)

    fun getBool(key: String, default: Boolean): Boolean

}
