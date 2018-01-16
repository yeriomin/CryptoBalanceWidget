package com.github.yeriomin.cryptobalancewidget.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context

abstract class AbstractWrapper(protected val context: Context) {

    protected val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun show(title: String, message: String) {
        manager.notify(title.hashCode(), getNotification(title, message))
    }

    abstract fun getNotification(title: String, message: String): Notification
}