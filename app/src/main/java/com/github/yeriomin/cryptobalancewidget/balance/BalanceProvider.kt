package com.github.yeriomin.cryptobalancewidget.balance

interface BalanceProvider {

    fun get(address: String): Double
}