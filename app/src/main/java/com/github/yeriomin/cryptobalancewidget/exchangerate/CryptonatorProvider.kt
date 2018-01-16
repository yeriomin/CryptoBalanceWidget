package com.github.yeriomin.cryptobalancewidget.exchangerate

import org.json.JSONObject
import java.net.URL

class CryptonatorProvider : ExchangeRateProvider {

    override fun getExchangeRate(from: String, to: String): Double {
        val o = JSONObject(URL("https://api.cryptonator.com/api/ticker/$from-$to").readText())
        return if (o.has("ticker") && o.getJSONObject("ticker").has("price")) o.getJSONObject("ticker").getString("price").toDouble() else 0.0
    }

    override fun listCurrencies(): Map<String, String> {
        val o = JSONObject(URL("https://www.cryptonator.com/api/currencies").readText())
        val result = HashMap<String, String>()
        if (!o.has("rows")) {
            return result
        }
        val a = o.getJSONArray("rows")
        (0 until a.length())
                .asSequence()
                .map { a[it] as JSONObject }
                .forEach { result.put(it.getString("name"), it.getString("code")) }
        return result
    }
}