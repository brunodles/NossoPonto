package com.brunodles.nossoponto

import android.content.Context
import android.preference.PreferenceManager

class Preferences(private val context: Context) {

    private val sharedPrefernces by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isFinisher(): Boolean = sharedPrefernces.getBoolean(PREF_KEY_FINISHER, false)

    fun setFinisher(value: Boolean) = sharedPrefernces.edit()
            .putBoolean(PREF_KEY_FINISHER, value)
            .apply()

    companion object {
        private const val PREF_KEY_FINISHER = "PREF_KEY_FINISHER"
    }

}