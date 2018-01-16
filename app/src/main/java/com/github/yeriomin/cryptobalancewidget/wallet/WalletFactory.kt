package com.github.yeriomin.cryptobalancewidget.wallet

class WalletFactory {

    companion object {
        fun get(address: String, currency: String): Wallet {
            return when (currency) {
                Wallet.BITCOIN  -> Bitcoin(address)
                Wallet.LITECOIN -> Litecoin(address)
                Wallet.DOGECOIN -> Dogecoin(address)
                Wallet.ETHEREUM -> Ethereum(address)
                else            -> throw Exception("Could not construct wallet for currency $currency")
            }
        }
    }
}