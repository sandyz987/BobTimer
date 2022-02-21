package com.sandyz.alltimers.concentrate

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.CONCENTRATE_ENTRY
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer.TimerDrawerImpl
import com.sandyz.alltimers.concentrate.service.TimerService
import com.sandyz.alltimers.concentrate.view.RollDiskView
import kotlinx.android.synthetic.main.concentrate_fragment_concentrate.*
import java.lang.ref.WeakReference


@Route(path = CONCENTRATE_ENTRY)
class FragmentConcentrate : BaseFragment() {

    private var service: TimerService? = null
    private var isBind = false
    private var onSecondsChange: ((Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.concentrate_fragment_concentrate, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 绑定服务
        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)

        // 注册广播接收者
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.sandyz.alltimers")
        intentFilter.priority = 100
        val timerReceiver = TimerReceiver(WeakReference(this))
        context.registerReceiver(timerReceiver, intentFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        concentrate_display.drawer = TimerDrawerImpl()
        concentrate_roll_disk.onTimeCountDownSetChange = { h, m ->
            val hours = if (h <= 9) "0$h" else "$h"
            val minutes = if (m <= 9) "0$m" else "$m"
            concentrate_display.mText = "${hours}时${minutes}分"
            Log.e("sandyzhangtime", "${hours}时 ${minutes}分")
        }
        onSecondsChange = { s ->
            concentrate_display.mText = RollDiskView.getTimeStr(s)
            concentrate_roll_disk.setSeconds(s)
        }
        concentrate_iv_switch.setOnClickAction {
            if (concentrate_roll_disk.isCountDown) {
                concentrate_iv_switch.text = "正计时"
                concentrate_roll_disk.isCountDown = false
                onSecondsChange?.invoke(0)
            } else {
                concentrate_iv_switch.text = "倒计时"
                onSecondsChange?.invoke(0)
                concentrate_roll_disk.isCountDown = true
            }
        }
        concentrate_roll_disk.onStartEvent = { isStarted ->
            concentrate_iv_switch.isEnabled = !isStarted
            /**
             * 设置布局变化
             */
            concentrate_guideline.setGuidelinePercent(0.5f)

        }
        concentrate_iv_start.setOnClickAction {
            if (concentrate_roll_disk.isStarted) {
                service?.stop()
                concentrate_iv_start.text = "开始专注"
                concentrate_roll_disk.isStarted = false
                if (!concentrate_roll_disk.isCountDown) {
                    onSecondsChange?.invoke(0)
                }
            } else {
                if (concentrate_roll_disk.isCountDown) {
                    if (concentrate_roll_disk.getCountDownTotalSeconds() != 0) {
                        concentrate_iv_start.text = "停止专注"
                        concentrate_roll_disk.isStarted = true
                        concentrate_roll_disk.setSeconds(concentrate_roll_disk.getCountDownTotalSeconds())
                        service?.startTimer(true, concentrate_roll_disk.getCountDownTotalSeconds())
                    }
                } else {
                    service?.startTimer(false, 0)
                    concentrate_iv_start.text = "停止专注"
                    concentrate_roll_disk.isStarted = true
                }
            }
        }


    }


    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            isBind = true
            val myBinder: TimerService.MyBinder = binder as TimerService.MyBinder
            service = myBinder.service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBind = false
        }
    }

    class TimerReceiver(private val fragment: WeakReference<FragmentConcentrate>) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val s = intent.getIntExtra("seconds", 0)
                fragment.get()?.onSecondsChange?.invoke(s)
            }
        }
    }


}