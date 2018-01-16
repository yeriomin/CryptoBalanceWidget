package com.github.yeriomin.cryptobalancewidget

import android.content.SharedPreferences

data class CryptoBalanceWidgetDao(private val preferences: SharedPreferences) {

    fun save(widget: CryptoBalanceWidget) {
        preferences.edit().putString(getKey(widget.id), widget.toString()).commit()
    }

    fun get(id: Int): CryptoBalanceWidget {
        return CryptoBalanceWidget(preferences.getString(getKey(id), ""))
    }

    fun exists(id: Int): Boolean {
        return !preferences.getString(getKey(id), "").isEmpty()
    }

    companion object {
        private fun getKey(id: Int):String {
            return "id_" + id
        }
    }
}