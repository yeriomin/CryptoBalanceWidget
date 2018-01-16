package com.github.yeriomin.cryptobalancewidget.wallet

class Dogecoin(address: String) : Wallet(address, DOGECOIN) {

    override fun valid(): Boolean {
        return """^D{1}[5-9A-HJ-NP-U]{1}[1-9A-HJ-NP-Za-km-z]{32}${'$'}""".toRegex().matches(address)
    }
}