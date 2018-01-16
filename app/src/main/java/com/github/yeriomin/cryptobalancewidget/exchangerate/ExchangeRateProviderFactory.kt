package com.github.yeriomin.cryptobalancewidget.exchangerate

class ExchangeRateProviderFactory {

    companion object {
        fun get(exchangeProvider: String): ExchangeRateProvider {
            return when (exchangeProvider) {
                ExchangeRateProvider.COINBASE         -> CoinbaseProvider()
                ExchangeRateProvider.CRYPTOCOINCHARTS -> CryptoCoinChartsProvider()
                ExchangeRateProvider.CRYPTONATOR      -> CryptonatorProvider()
                ExchangeRateProvider.EOBOT            -> EobotProvider()
                else                                  -> throw Exception("Could not construct exchange rate provider $exchangeProvider")
            }
        }
    }
}