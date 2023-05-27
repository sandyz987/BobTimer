package com.sandyz.alltimers.common

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.alibaba.android.arouter.launcher.ARouter


open class BaseApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak", "CI_StaticFieldLeak")
        lateinit var context: Context
            private set
        lateinit var app: Application
            private set

        const val foregroundService = "foreground"
        var time = 0L
        var typeface: Typeface? = null
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
        app = this
        time = System.currentTimeMillis()
        typeface = ResourcesCompat.getFont(context, R.font.summer_typeface)
    }

    override fun onCreate() {
        super.onCreate()
        initTypeface()
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

    private fun initTypeface() {
        Log.e("sandyzhang", "setfont")
        val field = Typeface::class.java.getDeclaredField("SERIF")
        field.isAccessible = true
        field.set(null, typeface)
    }

}