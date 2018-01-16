package com.github.yeriomin.cryptobalancewidget.exchangerate

interface ExchangeRateProvider {

    companion object {
        const val COINBASE = "Coinbase"
        const val CRYPTOCOINCHARTS = "CryptoCoinCharts"
        const val CRYPTONATOR = "Cryptonator"
        const val EOBOT = "Eobot"
    }

    fun getExchangeRate(from: String, to: String): Double
    fun listCurrencies(): Map<String, String>
}