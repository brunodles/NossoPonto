package com.brunodles.nossoponto

import android.content.Context
import android.preference.PreferenceManager

class Preferences(private val context: Context) {

    private val sharedPrefernces by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isFinisher(): Boolean = sharedPrefernces.getBoolean(KEY_FINISHER, false)

    fun setFinisher(value: Boolean) = sharedPrefernces.edit()
            .putBoolean(KEY_FINISHER, value)
            .apply()

    fun getPreviousUsername() :String? = sharedPrefernces.getString(KEY_PREVIOUS_USERNAME, null)

    fun setPreviousUsername(username: String) =
            sharedPrefernces.edit()
                    .putString(KEY_PREVIOUS_USERNAME, username)
                    .apply()

    fun clear() {
        sharedPrefernces.edit()
                .clear()
                .apply()
    }

    companion object {
        private const val KEY_FINISHER = "PREF_KEY_FINISHER"
        private const val KEY_PREVIOUS_USERNAME = "PREF_KEY_PREVIOUS_USERNAME"
    }

}