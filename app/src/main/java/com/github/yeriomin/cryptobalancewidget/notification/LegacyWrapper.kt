package com.github.yeriomin.cryptobalancewidget.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.github.yeriomin.cryptobalancewidget.R

class LegacyWrapper(context: Context) : AbstractWrapper(context) {

    override fun getNotification(title: String, message: String): Notification {
        val notification = Notification(R.drawable.ic_notification, "", System.currentTimeMillis())
        try {
            notification.javaClass
                    .getMethod("setLatestEventInfo", Context::class.java, CharSequence::class.java, CharSequence::class.java, PendingIntent::class.java)
                    .invoke(notification, context, title, message, PendingIntent.getActivity(context, 1, Intent(), 0))
            notification.flags = Notification.FLAG_AUTO_CANCEL
        } catch (e: Throwable) {
            // do nothing
        }
        return notification
    }
}