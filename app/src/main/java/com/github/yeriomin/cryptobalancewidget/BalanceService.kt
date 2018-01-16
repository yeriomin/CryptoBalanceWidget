package com.github.yeriomin.cryptobalancewidget

import android.app.IntentService
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.github.yeriomin.cryptobalancewidget.notification.NotificationWrapperFactory
import java.io.IOException
import java.text.DecimalFormat

class BalanceService:IntentService("BalanceService") {

    companion object {

        const val FORMAT_FIAT = "##########.##"
        const val FORMAT_CRYPTO = "##########.#####"

        fun getIntent(context: Context, widgetId: Int):Intent {
            val serviceIntent = Intent(context, BalanceService::class.java)
            serviceIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            serviceIntent.data = Uri.withAppendedPath(Uri.parse("my-widget-scheme://widget/id/"), widgetId.toString())
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            return serviceIntent
        }

        fun start(context: Context, widgetId: Int) {
            context.startService(getIntent(context, widgetId))
        }
    }

    override fun onHandleIntent(i: Intent?) {
        if (!i?.action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.e(javaClass.simpleName, "Unsupported action: " + i?.action)
            return
        }
        val widgetId = i?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (null == widgetId || widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.e(javaClass.simpleName, "No widget id in provided intent")
            return
        }
        Log.i(javaClass.simpleName, "Updating widget " + widgetId)
        try {
            update(widgetId)
        } catch (e: Throwable) {
            Log.e(javaClass.simpleName, "${e.javaClass.simpleName} ${e.message}")
            e.printStackTrace()
        }
    }

    private fun update(widgetId: Int) {
        val widgetManager = AppWidgetManager.getInstance(applicationContext)
        val views = RemoteViews(applicationContext.packageName, R.layout.crypto_balance_widget)
        views.setViewVisibility(R.id.balance, View.GONE)
        views.setViewVisibility(R.id.progress, View.VISIBLE)
        val widget = getWidget(widgetId)
        views.setTextViewText(R.id.wallet_address, widget?.walletAddress)
        views.setTextViewText(R.id.description, widget?.description)
        widgetManager.updateAppWidget(widgetId, views)

        if (null != widget) {
            try {
                views.setTextViewText(R.id.balance, getBalanceTextAndNotify(widget))
            } catch (e: IOException) {
                Log.e(javaClass.simpleName, "Could not get balance because of ${e.javaClass.simpleName}: ${e.message}")
            }
        }
        views.setViewVisibility(R.id.balance, View.VISIBLE)
        views.setViewVisibility(R.id.progress, View.GONE)
        views.setOnClickPendingIntent(
                R.id.widget_layout,
                PendingIntent.getService(
                        applicationContext,
                        0,
                        getIntent(applicationContext, widgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
        )
        widgetManager.updateAppWidget(widgetId, views)
    }

    private fun getBalanceTextAndNotify(widget: CryptoBalanceWidget): String {
        var balance = widget.getWallet().getBalance()
        Log.i(javaClass.simpleName, "Balance: " + balance + " " + widget.walletCurrency)
        val shouldNotify = balance > 0 && balance != widget.previousBalance && widget.notify
        widget.previousBalance = balance
        if (!TextUtils.isEmpty(widget.displayCurrency) && widget.walletCurrency != widget.displayCurrency) {
            val exchangeRate = widget.getExchangeProvider().getExchangeRate(widget.walletCurrency, widget.displayCurrency)
            Log.i(javaClass.simpleName, "Exchange rate: " + exchangeRate + " " + widget.displayCurrency + " (" + widget.exchangeProvider + ")")
            balance *= exchangeRate
        }
        val balanceText = formatBalance(widget, balance)
        if (shouldNotify) {
            Log.i(javaClass.simpleName, "Notifying about balance change")
            notifyBalanceChanged(if (TextUtils.isEmpty(widget.description)) widget.walletAddress else widget.description, balanceText)
            CryptoBalanceWidgetDao(PreferenceManager.getDefaultSharedPreferences(this)).save(widget)
        }
        return balanceText
    }

    private fun formatBalance(widget: CryptoBalanceWidget, balance: Double): String {
        return "${DecimalFormat(
                if (resources.getStringArray(R.array.wallet_currencies).toSet().contains(widget.getCurrencySuffix()))
                    FORMAT_CRYPTO
                else
                    FORMAT_FIAT
        ).format(balance)} ${widget.getCurrencySuffix()}"
    }

    private fun getWidget(id: Int): CryptoBalanceWidget? {
        val dao = CryptoBalanceWidgetDao(PreferenceManager.getDefaultSharedPreferences(this))
        if (!dao.exists(id)) {
            Log.i(javaClass.simpleName, "Widget $id is not configured yet")
            return null
        }
        val widget = dao.get(id)
        Log.i(javaClass.simpleName, "Raw widget object: " + widget.toString())
        return widget
    }

    private fun notifyBalanceChanged(walletTag: String, balanceText: String) {
        NotificationWrapperFactory.get(applicationContext).show(
                applicationContext.getString(R.string.notification_title_balance_changed, walletTag),
                applicationContext.getString(R.string.notification_message_balance_changed, balanceText)
        )
    }
}