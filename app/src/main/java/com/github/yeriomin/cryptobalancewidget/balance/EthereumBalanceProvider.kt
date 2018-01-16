package com.github.yeriomin.cryptobalancewidget.balance

import org.json.JSONObject
import java.net.URL

class EthereumBalanceProvider : BalanceProvider {

    override fun get(address: String): Double {
        val o = JSONObject(URL("https://api.etherscan.io/api?module=account&action=balance&address=$address&tag=latest").readText())
        return if (o.has("status") && o.has("result") && o.get("status") == "1") o.getString("result").toDouble() / 1000000000000000000 else 0.0
    }
}