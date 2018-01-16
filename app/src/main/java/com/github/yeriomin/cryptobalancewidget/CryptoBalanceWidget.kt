package com.github.yeriomin.cryptobalancewidget

import android.text.TextUtils
import com.github.yeriomin.cryptobalancewidget.exchangerate.ExchangeRateProvider
import com.github.yeriomin.cryptobalancewidget.exchangerate.ExchangeRateProviderFactory
import com.github.yeriomin.cryptobalancewidget.wallet.Wallet
import com.github.yeriomin.cryptobalancewidget.wallet.WalletFactory
import org.json.JSONObject

data class CryptoBalanceWidget(var id: Int = 0, var walletAddress: String = "", var walletCurrency: String = "", var displayCurrency: String = "", var exchangeProvider: String = "", var description: String = "", var notify: Boolean = false, var previousBalance: Double = 0.0) {

    companion object {
        val FIELD_ID = "FIELD_ID"
        val FIELD_WALLET_ADDRESS = "FIELD_WALLET_ADDRESS"
        val FIELD_WALLET_CURRENCY = "FIELD_WALLET_CURRENCY"
        val FIELD_DISPLAY_CURRENCY = "FIELD_DISPLAY_CURRENCY"
        val FIELD_EXCHANGE_PROVIDER = "FIELD_EXCHANGE_PROVIDER"
        val FIELD_DESCRIPTION = "FIELD_DESCRIPTION"
        val FIELD_NOTIFY = "FIELD_NOTIFY"
        val FIELD_PREVIOUS_BALANCE = "FIELD_PREVIOUS_BALANCE"
    }

    constructor(jsonString: String):this(0, "", "", "", "", "", false, 0.0) {
        val o = JSONObject(jsonString)
        id = o.getInt(FIELD_ID)
        walletAddress = o.optString(FIELD_WALLET_ADDRESS, "")
        walletCurrency = o.optString(FIELD_WALLET_CURRENCY, "")
        displayCurrency = o.optString(FIELD_DISPLAY_CURRENCY, "")
        exchangeProvider = o.optString(FIELD_EXCHANGE_PROVIDER, "")
        description = o.optString(FIELD_DESCRIPTION, "")
        notify = o.optBoolean(FIELD_NOTIFY, false)
        previousBalance = o.optDouble(FIELD_PREVIOUS_BALANCE, 0.0)
    }

    override fun toString(): String {
        val o = JSONObject()
        o.put(FIELD_ID, id)
        o.put(FIELD_WALLET_ADDRESS, walletAddress)
        o.put(FIELD_WALLET_CURRENCY, walletCurrency)
        o.put(FIELD_DISPLAY_CURRENCY, displayCurrency)
        o.put(FIELD_EXCHANGE_PROVIDER, exchangeProvider)
        o.put(FIELD_DESCRIPTION, description)
        o.put(FIELD_NOTIFY, notify)
        o.put(FIELD_PREVIOUS_BALANCE, previousBalance)
        return o.toString()
    }

    fun getWallet():Wallet {
        return WalletFactory.get(walletAddress, walletCurrency)
    }

    fun getExchangeProvider():ExchangeRateProvider {
        return ExchangeRateProviderFactory.get(exchangeProvider)
    }

    fun getCurrencySuffix(): String {
        return if (TextUtils.isEmpty(displayCurrency)) walletCurrency else displayCurrency
    }
}