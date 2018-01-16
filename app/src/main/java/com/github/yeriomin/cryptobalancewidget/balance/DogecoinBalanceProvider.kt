package com.github.yeriomin.cryptobalancewidget.balance

import java.net.URL

class DogecoinBalanceProvider : BalanceProvider {

    override fun get(address: String): Double {
        return URL("https://dogechain.info/chain/Dogecoin/q/addressbalance/" + address).readText().toDouble()
    }
}