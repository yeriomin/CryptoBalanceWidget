package com.github.yeriomin.cryptobalancewidget.wallet

class Litecoin(address: String) : Wallet(address, LITECOIN) {

    override fun valid(): Boolean {
        return """^L[a-zA-Z0-9]{26,33}${'$'}""".toRegex().matches(address)
    }
}