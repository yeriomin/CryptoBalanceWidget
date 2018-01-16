package com.github.yeriomin.cryptobalancewidget.wallet

import com.github.yeriomin.cryptobalancewidget.balance.BalanceProviderFactory

abstract class Wallet(val address: String, val currency: String) {

    companion object {
        const val BITCOIN = "BTC"
        const val LITECOIN = "LTC"
        const val DOGECOIN = "DOGE"
        const val ETHEREUM = "ETH"
    }

    abstract fun valid(): Boolean

    fun getBalance(): Double {
        return BalanceProviderFactory.get(currency).get(address)
    }
}