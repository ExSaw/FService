package com.rickrip.fservice.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.rickrip.fservice.util.Singleton.FAPP_NOTIFICATION_CHANNEL_NAME

class FApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            FAPP_NOTIFICATION_CHANNEL_NAME,
            "FServiceApp Notifications",
            NotificationManager.IMPORTANCE_MIN
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}