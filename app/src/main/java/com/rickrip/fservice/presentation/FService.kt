package com.rickrip.fservice.presentation

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.rickrip.fservice.R
import com.rickrip.fservice.di.IDispatchersProvider
import com.rickrip.fservice.di.KoinQualifiers
import com.rickrip.fservice.util.ICommunicator
import com.rickrip.fservice.util.Singleton.FAPP_NOTIFICATION_CHANNEL_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.random.Random


class FService : Service() {

    private companion object {
        const val ID = 1
    }

    private val communicator: ICommunicator by inject()
    private val appScope: CoroutineScope by inject(KoinQualifiers.APP_SCOPE.qualifier)
    private val dispatchers: IDispatchersProvider by inject()
    private var subscriberJob: Job? = null

    private val notificationManager by lazy {
        getSystemService(NotificationManager::class.java)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MyServiceAction.START.toString() -> {
                start()
                startSubscriber()
            }

            MyServiceAction.STOP.toString() -> {
                stopSelf()
                subscriberJob?.cancel()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("ForegroundServiceType")
    private fun start() {
        startForeground(ID, getNotification(""))
    }

    private fun getNotification(progress: String) : Notification =
        Notification.Builder(this, FAPP_NOTIFICATION_CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Uploading files")
            .setContentText("Total Progress=$progress")
            .build()

    private fun updateNotification(text: String) {
        notificationManager.notify(
            ID,
            getNotification(text)
        )
    }

    private fun startSubscriber() {
        subscriberJob?.cancel()
        subscriberJob = appScope.launch(dispatchers.default) {
            communicator.getFlow().collectLatest {
                updateNotification(it.toString())
            }
        }
    }

    enum class MyServiceAction {
        START, STOP
    }
}
