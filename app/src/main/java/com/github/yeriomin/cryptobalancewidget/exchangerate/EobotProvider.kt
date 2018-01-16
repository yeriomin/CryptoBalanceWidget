package com.github.yeriomin.cryptobalancewidget.exchangerate

import java.net.URL

class EobotProvider : ExchangeRateProvider {

    override fun getExchangeRate(from: String, to: String): Double {
        return URL("https://www.eobot.com/api.aspx?coin=" + from).readText().toDouble() * URL("https://www.eobot.com/api.aspx?coin=" + to).readText().toDouble()
    }

    override fun listCurrencies(): Map<String, String> {
        return URL("https://www.eobot.com/api.aspx?supportedfiat=true")
                .readText()
                .split(";")
                .associateBy({it.split(",")[0]}, {it.split(",")[0]})
    }
}