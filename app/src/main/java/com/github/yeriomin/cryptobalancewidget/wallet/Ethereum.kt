package com.github.yeriomin.cryptobalancewidget.wallet

class Ethereum(address: String) : Wallet(address, ETHEREUM) {

    override fun valid(): Boolean {
        return """^0x[a-fA-F0-9]{40}${'$'}""".toRegex().matches(address)
    }
}