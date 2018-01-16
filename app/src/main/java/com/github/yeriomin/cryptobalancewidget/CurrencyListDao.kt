package com.github.yeriomin.cryptobalancewidget

import android.content.SharedPreferences
import org.json.JSONObject

class CurrencyListDao(private val preferences: SharedPreferences) {

    private val CURRENCY_LIST = "CURRENCY_LIST_"

    fun get(provider: String): Map<String, String> {
        val o = JSONObject(preferences.getString(CURRENCY_LIST + provider, ""))
        val result = HashMap<String, String>()
        o.keys().forEach { result.put(it, o.getString(it)) }
        return result
    }

    fun save(provider: String, currencies: Map<String, String>) {
        preferences.edit().putString(CURRENCY_LIST + provider, JSONObject(currencies).toString()).commit()
    }

    fun exists(provider: String): Boolean {
        return !preferences.getString(CURRENCY_LIST + provider, "").isEmpty()
    }
}