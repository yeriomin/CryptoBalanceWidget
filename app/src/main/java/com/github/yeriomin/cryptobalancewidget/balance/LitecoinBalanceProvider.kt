package com.github.yeriomin.cryptobalancewidget.balance

import org.json.JSONObject
import java.net.URL

class LitecoinBalanceProvider : BalanceProvider {

    override fun get(address: String): Double {
        val o = JSONObject(URL("https://api.blockcypher.com/v1/ltc/main/addrs/$address").readText())
        return if (o.has("balance")) o.getString("balance").toDouble() / 100000000 else 0.0
    }
}