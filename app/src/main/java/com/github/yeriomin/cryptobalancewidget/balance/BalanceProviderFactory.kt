package com.github.yeriomin.cryptobalancewidget.balance

import com.github.yeriomin.cryptobalancewidget.wallet.Wallet

class BalanceProviderFactory {

    companion object {
        fun get(currency: String): BalanceProvider {
            return when (currency) {
                Wallet.BITCOIN  -> BitcoinBalanceProvider()
                Wallet.LITECOIN -> LitecoinBalanceProvider()
                Wallet.DOGECOIN -> DogecoinBalanceProvider()
                Wallet.ETHEREUM -> EthereumBalanceProvider()
                else            -> throw Exception("Could not choose BalanceProvider for currency $currency")
            }
        }
    }
}