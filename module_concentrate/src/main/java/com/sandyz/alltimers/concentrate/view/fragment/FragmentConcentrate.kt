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
import com.sandyz.alltimers.concentrate.databinding.ConcentrateFragmentConcentrateBinding
import com.sandyz.alltimers.concentrate.service.TimerService
import com.sandyz.alltimers.concentrate.view.adapter.ConcentrateBackgroundAdapter
import com.sandyz.alltimers.concentrate.view.costom.RollDiskView
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.lang.ref.WeakReference


@Route(path = CONCENTRATE_ENTRY)
class FragmentConcentrate : BaseFragment() {

    private var service: TimerService? = null
    private var isBind = false
    private var onSecondsChange: ((Int) -> Unit)? = null
    private var timerReceiver: TimerReceiver? = null
    private lateinit var binding: ConcentrateFragmentConcentrateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ConcentrateFragmentConcentrateBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
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
        binding.apply {
            concentrateRvBg.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            val bgAdapter = ConcentrateBackgroundAdapter(this@FragmentConcentrate, concentrateRvBg)
            concentrateRvBg.adapter = bgAdapter
            OverScrollDecoratorHelper.setUpOverScroll(concentrateRvBg, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(concentrate_rv_bg)


            concentrateDisplay.drawer = TimerDrawerImpl()
            concentrateRollDisk.onTimeCountDownSetChange = { h, m ->
                val hours = if (h <= 9) "0$h" else "$h"
                val minutes = if (m <= 9) "0$m" else "$m"
                concentrateDisplay.mText = "${hours}时${minutes}分"
                Log.e("sandyzhangtime", "${hours}时 ${minutes}分")
            }
            onSecondsChange = { s ->
                concentrateDisplay.mText = RollDiskView.getTimeStr(s)
                concentrateRollDisk.setSeconds(s)
            }
            concentrateIvSwitch.setOnClickAction {
                setCountDown(!concentrateRollDisk.isCountDown)
            }
            concentrateRollDisk.onStartEvent = { isStarted ->
                concentrateIvSwitch.isEnabled = !isStarted
                /**
                 * 设置布局变化
                 */
                if (isStarted) {
                    startTimerAnim()
                } else {
                    stopTimerAnim()
                }
            }
            concentrateIvStart.setOnClickAction {
                switch(!concentrateRollDisk.isStarted)
            }
        }

    }

    fun setCountDown(countDown: Boolean) {
        if (countDown == binding.concentrateRollDisk.isCountDown) return
        if (!countDown) {
            binding.concentrateIvSwitch.text = "正计时"
            binding.concentrateRollDisk.isCountDown = false
            onSecondsChange?.invoke(0)
        } else {
            binding.concentrateIvSwitch.text = "倒计时"
            onSecondsChange?.invoke(0)
            binding.concentrateRollDisk.isCountDown = true
        }
    }

    fun switch(start: Boolean) {
        binding.apply {
            if (start == concentrateRollDisk.isStarted) return
            if (!start) {
                service?.stop()
                concentrateIvStart.text = "开始专注"
                concentrateRollDisk.isStarted = false
                if (concentrateRollDisk.isCountDown != true) {
                    onSecondsChange?.invoke(0)
                }
            } else {
                if (concentrateRollDisk.isCountDown == true) {
                    if (concentrateRollDisk.getCountDownTotalSeconds() != 0) {
                        concentrateIvStart.text = "停止专注"
                        concentrateRollDisk.isStarted = true
                        concentrateRollDisk.setSeconds(concentrateRollDisk.getCountDownTotalSeconds())
                        concentrateRollDisk.getCountDownTotalSeconds().let { service?.startTimer(true, it) }
                    }
                } else {
                    service?.startTimer(false, 0)
                    concentrateIvStart.text = "停止专注"
                    concentrateRollDisk.isStarted = true
                }
            }
        }

    }

    fun setBackground(@DrawableRes id: Int) {
        binding.concentrateClBg.setBackgroundResource(id)
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
                binding.concentrateRvBg.alpha = 1 - it.animatedFraction
                binding.concentrateGuideline.setGuidelinePercent(it.animatedValue as Float)
            }
            doOnEnd {
                binding.concentrateRvBg.visibility = View.GONE
            }
            start()
        }
    }

    private fun stopTimerAnim() {
        layoutAnimator?.cancel()
        binding.concentrateRvBg.visibility = View.VISIBLE
        layoutAnimator = ValueAnimator.ofFloat(0.55f, 0.5f).apply {
            duration = 500
            addUpdateListener {
                binding.concentrateRvBg.alpha = it.animatedFraction
                binding.concentrateGuideline.setGuidelinePercent(it.animatedValue as Float)
            }
            start()
        }
    }


}