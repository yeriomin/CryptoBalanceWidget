package com.github.yeriomin.cryptobalancewidget.notification

import android.annotation.TargetApi
import android.app.Notification
import android.content.Context
import android.os.Build
import com.github.yeriomin.cryptobalancewidget.R

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class HoneycombWrapper(context: Context) : AbstractWrapper(context) {

    override fun getNotification(title: String, message: String): Notification {
        return Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .notification
    }
}