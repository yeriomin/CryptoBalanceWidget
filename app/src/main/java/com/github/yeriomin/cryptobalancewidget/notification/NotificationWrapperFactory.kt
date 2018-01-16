package com.github.yeriomin.cryptobalancewidget.notification

import android.content.Context
import android.os.Build

class NotificationWrapperFactory {

    companion object {
        fun get(context: Context): AbstractWrapper {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O         -> OreoWrapper(context)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB -> HoneycombWrapper(context)
                else                                                   -> LegacyWrapper(context)
            }
        }
    }
}