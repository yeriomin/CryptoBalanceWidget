package com.github.yeriomin.cryptobalancewidget.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.github.yeriomin.cryptobalancewidget.BuildConfig
import com.github.yeriomin.cryptobalancewidget.R

@TargetApi(Build.VERSION_CODES.O)
class OreoWrapper(context: Context) : AbstractWrapper(context) {

    init {
        val channel = manager.getNotificationChannel(BuildConfig.APPLICATION_ID)
        if (null == channel) {
            manager.createNotificationChannel(NotificationChannel(
                    BuildConfig.APPLICATION_ID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            ))
        }
    }

    override fun getNotification(title: String, message: String): Notification {
        return Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setChannelId(BuildConfig.APPLICATION_ID)
                .build()
    }
}