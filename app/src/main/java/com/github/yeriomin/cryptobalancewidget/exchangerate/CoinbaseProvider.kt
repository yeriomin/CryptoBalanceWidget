package com.github.yeriomin.cryptobalancewidget.exchangerate

import org.json.JSONObject
import java.net.URL

class CoinbaseProvider : ExchangeRateProvider {

    override fun getExchangeRate(from: String, to: String): Double {
        val o = JSONObject(URL("https://api.coinbase.com/v2/prices/$from-$to/spot").readText())
        return if (o.has("data") && o.getJSONObject("data").has("amount")) o.getJSONObject("data").getString("amount").toDouble() else 0.0
    }

    override fun listCurrencies(): Map<String, String> {
        val o = JSONObject(URL("https://api.coinbase.com/v2/currencies").readText())
        val result = HashMap<String, String>()
        if (!o.has("data")) {
            return result
        }
        val a = o.getJSONArray("data")
        (0 until a.length())
                .asSequence()
                .map { a[it] as JSONObject }
                .forEach { result.put(it.getString("name"), it.getString("id")) }
        return result
    }
}