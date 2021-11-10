package com.sandyz.alltimers.common

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.alibaba.android.arouter.launcher.ARouter


open class BaseApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak", "CI_StaticFieldLeak")
        lateinit var context: Context
            private set

        const val foregroundService = "foreground"
        var time = 0L
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
        time = System.currentTimeMillis()
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        initRouter()
    }

    private fun initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "计时"
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel = NotificationChannel(foregroundService, name, importance)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}