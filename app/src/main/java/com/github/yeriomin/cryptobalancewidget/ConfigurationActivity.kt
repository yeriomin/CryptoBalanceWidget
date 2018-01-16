package com.github.yeriomin.cryptobalancewidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.github.yeriomin.cryptobalancewidget.exchangerate.ExchangeRateProviderFactory
import kotlin.concurrent.thread

class ConfigurationActivity:Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.configuration_activity)
        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
        Log.i(javaClass.simpleName, "Configuring widget " + widgetId)
        prepareForm(widgetId)
    }

    private fun prepareForm(widgetId: Int) {
        findViewById<Button>(R.id.configuration_ok).setOnClickListener {
            val cryptoBalanceWidget = getCryptoBalanceWidget()
            cryptoBalanceWidget.id = widgetId
            if (!validate(cryptoBalanceWidget)) {
                return@setOnClickListener
            }
            CryptoBalanceWidgetDao(PreferenceManager.getDefaultSharedPreferences(this)).save(cryptoBalanceWidget)

            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
            BalanceService.start(this, widgetId)
        }
        val displayCurrencyView = findViewById<AutoCompleteTextView>(R.id.configuration_display_currency)
        val exchangeView = findViewById<Spinner>(R.id.configuration_exchange_provider)
        exchangeView.isEnabled = false
        findViewById<CheckBox>(R.id.configuration_convert).setOnCheckedChangeListener { _, b -> run {
            displayCurrencyView.isEnabled = b
            exchangeView.isEnabled = b
        } }
        exchangeView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                thread(true) {
                    fillCurrencyList(resources.getStringArray(R.array.exchange_providers)[position], displayCurrencyView)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        thread(true) {
            fillCurrencyList(resources.getStringArray(R.array.exchange_providers)[exchangeView.selectedItemPosition], displayCurrencyView)
        }
    }

    private fun fillCurrencyList(providerName: String, displayCurrencyView: AutoCompleteTextView) {
        val currencyMap: Map<String, String>
        val currencyListDao = CurrencyListDao(PreferenceManager.getDefaultSharedPreferences(this))
        try {
            if (currencyListDao.exists(providerName)) {
                currencyMap = currencyListDao.get(providerName)
            } else {
                Log.i(javaClass.simpleName, "Getting currency list for $providerName")
                currencyMap = ExchangeRateProviderFactory.get(providerName).listCurrencies()
                currencyListDao.save(providerName, currencyMap)
            }
            runOnUiThread {
                displayCurrencyView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, currencyMap.keys.toList()))
                displayCurrencyView.setOnItemClickListener { _, view, _, _ ->  displayCurrencyView.tag = currencyMap[(view as TextView).text] }
            }
        } catch (e: Throwable) {
            Log.e(javaClass.simpleName, "Could not fill currency list: ${e.javaClass.simpleName} ${e.message}")
        }
    }

    private fun getCryptoBalanceWidget():CryptoBalanceWidget {
        val cryptoBalanceWidget = CryptoBalanceWidget()
        cryptoBalanceWidget.walletAddress = (findViewById<EditText>(R.id.configuration_wallet_address) as EditText).text.toString()
        cryptoBalanceWidget.walletCurrency = (findViewById<Spinner>(R.id.configuration_wallet_currency) as Spinner).selectedItem.toString()
        val displayCurrencyView = findViewById<AutoCompleteTextView>(R.id.configuration_display_currency)
        cryptoBalanceWidget.displayCurrency = if (null != displayCurrencyView.tag) displayCurrencyView.tag.toString() else ""
        cryptoBalanceWidget.exchangeProvider = (findViewById<Spinner>(R.id.configuration_exchange_provider) as Spinner).selectedItem.toString()
        cryptoBalanceWidget.description = (findViewById<Spinner>(R.id.configuration_description) as EditText).text.toString()
        cryptoBalanceWidget.notify = (findViewById<CheckBox>(R.id.configuration_notify) as CheckBox).isChecked
        return cryptoBalanceWidget
    }

    private fun validate(widget: CryptoBalanceWidget): Boolean {
        var result = true
        if (!widget.getWallet().valid()) {
            result = false
            (findViewById<EditText>(R.id.configuration_wallet_address) as EditText).error = getString(R.string.configuration_invalid_wallet_address)
        }
        if (findViewById<CheckBox>(R.id.configuration_convert).isChecked
            && (TextUtils.isEmpty(widget.displayCurrency)
                || widget.displayCurrency.toLowerCase() == widget.walletCurrency.toLowerCase()
            )
        ) {
            result = false
            (findViewById<AutoCompleteTextView>(R.id.configuration_display_currency) as AutoCompleteTextView).error = getString(R.string.configuration_invalid_display_currency)
        }
        return result
    }
}
