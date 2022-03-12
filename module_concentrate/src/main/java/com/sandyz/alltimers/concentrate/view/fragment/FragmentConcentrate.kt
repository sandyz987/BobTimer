package com.sandyz.alltimers.concentrate.view.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.CONCENTRATE_ENTRY
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer.TimerDrawerImpl
import com.sandyz.alltimers.concentrate.R
import com.sandyz.alltimers.concentrate.service.TimerService
import com.sandyz.alltimers.concentrate.view.adapter.ConcentrateBackgroundAdapter
import com.sandyz.alltimers.concentrate.view.costom.RollDiskView
import kotlinx.android.synthetic.main.concentrate_fragment_concentrate.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.lang.ref.WeakReference


@Route(path = CONCENTRATE_ENTRY)
class FragmentConcentrate : BaseFragment() {

    private var service: TimerService? = null
    private var isBind = false
    private var onSecondsChange: ((Int) -> Unit)? = null
    private var timerReceiver: TimerReceiver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.concentrate_fragment_concentrate, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onStart() {
        super.onStart()
        Log.e("concentrate", "fragment:$this")

        // 绑定服务
        val intent = Intent(context, TimerService::class.java)
        context?.bindService(intent, conn, Context.BIND_AUTO_CREATE)

        // 注册广播接收者
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.sandyz.alltimers")
        intentFilter.priority = 100
        timerReceiver = TimerReceiver(WeakReference(this))
        context?.registerReceiver(timerReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        context?.unregisterReceiver(timerReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        service?.stop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        concentrate_rv_bg.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        val bgAdapter = ConcentrateBackgroundAdapter(this, concentrate_rv_bg)
        concentrate_rv_bg.adapter = bgAdapter
        OverScrollDecoratorHelper.setUpOverScroll(concentrate_rv_bg, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(concentrate_rv_bg)


        concentrate_display.drawer = TimerDrawerImpl()
        concentrate_roll_disk?.onTimeCountDownSetChange = { h, m ->
            val hours = if (h <= 9) "0$h" else "$h"
            val minutes = if (m <= 9) "0$m" else "$m"
            concentrate_display?.mText = "${hours}时${minutes}分"
            Log.e("sandyzhangtime", "${hours}时 ${minutes}分")
        }
        onSecondsChange = { s ->
            concentrate_display?.mText = RollDiskView.getTimeStr(s)
            concentrate_roll_disk?.setSeconds(s)
        }
        concentrate_iv_switch.setOnClickAction {
            setCountDown(!concentrate_roll_disk.isCountDown)
        }
        concentrate_roll_disk.onStartEvent = { isStarted ->
            concentrate_iv_switch.isEnabled = !isStarted
            /**
             * 设置布局变化
             */
            if (isStarted) {
                startTimerAnim()
            } else {
                stopTimerAnim()
            }
        }
        concentrate_iv_start.setOnClickAction {
            switch(!concentrate_roll_disk.isStarted)
        }


    }

    fun setCountDown(countDown: Boolean) {
        if (countDown == concentrate_roll_disk.isCountDown) return
        if (!countDown) {
            concentrate_iv_switch.text = "正计时"
            concentrate_roll_disk.isCountDown = false
            onSecondsChange?.invoke(0)
        } else {
            concentrate_iv_switch.text = "倒计时"
            onSecondsChange?.invoke(0)
            concentrate_roll_disk.isCountDown = true
        }
    }

    fun switch(start: Boolean) {
        if (start == concentrate_roll_disk?.isStarted) return
        if (!start) {
            service?.stop()
            concentrate_iv_start?.text = "开始专注"
            concentrate_roll_disk?.isStarted = false
            if (concentrate_roll_disk?.isCountDown != true) {
                onSecondsChange?.invoke(0)
            }
        } else {
            if (concentrate_roll_disk?.isCountDown == true) {
                if (concentrate_roll_disk?.getCountDownTotalSeconds() != 0) {
                    concentrate_iv_start?.text = "停止专注"
                    concentrate_roll_disk?.isStarted = true
                    concentrate_roll_disk?.setSeconds(concentrate_roll_disk.getCountDownTotalSeconds())
                    concentrate_roll_disk?.getCountDownTotalSeconds()?.let { service?.startTimer(true, it) }
                }
            } else {
                service?.startTimer(false, 0)
                concentrate_iv_start?.text = "停止专注"
                concentrate_roll_disk?.isStarted = true
            }
        }
    }

    fun setBackground(@DrawableRes id: Int) {
        concentrate_cl_bg.setBackgroundResource(id)
    }


    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            isBind = true
            val myBinder: TimerService.MyBinder = binder as TimerService.MyBinder
            service = myBinder.service
            Log.e("concentrate", "connected")

        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("concentrate", "disconnected")
            isBind = false
        }
    }

    class TimerReceiver(private val fragment: WeakReference<FragmentConcentrate>) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val seconds = intent.getIntExtra("seconds", 0)
                val isCountDown = intent.getBooleanExtra("isCountDown", false)
                Log.e("concentrate", "seconds:$seconds, isCountDown:$isCountDown")
                fragment.get()?.setCountDown(isCountDown)
                fragment.get()?.switch(true)
                fragment.get()?.onSecondsChange?.invoke(seconds)
            }
        }
    }


    private var layoutAnimator: Animator? = null
    private fun startTimerAnim() {
        layoutAnimator?.cancel()
        layoutAnimator = ValueAnimator.ofFloat(0.5f, 0.55f).apply {
            duration = 500
            addUpdateListener {
                concentrate_rv_bg.alpha = 1 - it.animatedFraction
                concentrate_guideline.setGuidelinePercent(it.animatedValue as Float)
            }
            doOnEnd {
                concentrate_rv_bg.visibility = View.GONE
            }
            start()
        }
    }

    private fun stopTimerAnim() {
        layoutAnimator?.cancel()
        concentrate_rv_bg.visibility = View.VISIBLE
        layoutAnimator = ValueAnimator.ofFloat(0.55f, 0.5f).apply {
            duration = 500
            addUpdateListener {
                concentrate_rv_bg.alpha = it.animatedFraction
                concentrate_guideline.setGuidelinePercent(it.animatedValue as Float)
            }
            start()
        }
    }


}