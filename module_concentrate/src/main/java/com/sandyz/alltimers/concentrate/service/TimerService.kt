package com.sandyz.alltimers.concentrate.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder

class TimerService : Service() {
    private val myBinder: MyBinder = MyBinder()

    inner class MyBinder : Binder() {
        val service: TimerService
            get() = this@TimerService
    }

    private fun sendBroadcastCondition(seconds: Int) {
        val intent = Intent("com.sandyz.alltimers");
        intent.putExtra("seconds", seconds);
        sendBroadcast(intent);
    }

    private var isCountDown = false
    private var currentSeconds = 0

    fun startTimer(isCountDown: Boolean, seconds: Int) {
        this.isCountDown = isCountDown
        currentSeconds = seconds
        handler.removeCallbacks(perSecondRunnable)
        handler.post(perSecondRunnable)
    }

    private val handler = Handler()
    private val perSecondRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isCountDown) {
                currentSeconds -= 1
            } else {
                currentSeconds += 1
            }
            sendBroadcastCondition(currentSeconds)
            if (!(isCountDown && currentSeconds == 0)) {
                handler.postDelayed(this, 1000L)
            }
        }
    }

    fun stop() {
        handler.removeCallbacks(perSecondRunnable)
    }

    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
//        Timer timer = new Timer();
//        timer.schedule(timerTask,1000,200);
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }
}