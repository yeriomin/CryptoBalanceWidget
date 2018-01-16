package com.github.yeriomin.cryptobalancewidget.exchangerate

import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class CryptoCoinChartsProvider : ExchangeRateProvider {

    override fun getExchangeRate(from: String, to: String): Double {
        val o = JSONObject(URL("http://api.cryptocoincharts.info/tradingPair/${from}_$to").readText())
        return if (o.has("price")) o.getString("price").toDouble() else 0.0
    }

    override fun listCurrencies(): Map<String, String> {
        val a = JSONArray(URL("http://api.cryptocoincharts.info/listCoins").readText())
        val result = HashMap<String, String>()
        (0 until a.length())
            .asSequence()
            .map { a[it] as JSONObject }
            .forEach { result.put(it.getString("name"), it.getString("id")) }
        return result
    }
}