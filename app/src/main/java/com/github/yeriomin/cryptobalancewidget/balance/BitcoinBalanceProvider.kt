package com.github.yeriomin.cryptobalancewidget.balance

import java.net.URL

class BitcoinBalanceProvider : BalanceProvider {

    override fun get(address: String): Double {
        return URL("https://blockchain.info/q/addressbalance/" + address).readText().toDouble() / 100000000
    }
}