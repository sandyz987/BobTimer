package com.sandyz.alltimers.main.view.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.MAIN_MAIN
import com.sandyz.alltimers.common.config.SCHEDULE_EDIT
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.LogUtils
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.databinding.MainActivityMainBinding
import com.sandyz.alltimers.main.view.adapter.MainAdapter

@Route(path = MAIN_MAIN)
class ActivityMain : BaseActivity() {

    private lateinit var binding: MainActivityMainBinding
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.e(
            "sandyzhang1 activity onTouchEvent!! ${
                when (event?.action) {
                    0 -> "Down"
                    1 -> "UP"
                    2 -> "Move"
                    else -> "Unknown"
                }
            }"
        )
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)
        DataBindingUtil.setContentView<MainActivityMainBinding>(this, R.layout.main_activity_main).also {
            binding = it
        }
        binding.apply {
            vpMain.adapter = MainAdapter(this@ActivityMain)
            vpMain.setCurrentItem(0, false)
            vpMain.isUserInputEnabled = false
            vpMain.offscreenPageLimit = 5


//        dynamic_time.setOnClickListener {
//            val r = Random(System.currentTimeMillis())
//            val stringBuilder = StringBuilder()
//            (0 until 4).forEach { _ ->
//                stringBuilder.append(r.nextInt(0,2).toChar() + '0'.toInt())
//            }
//            Log.e("sandyzhang", "text:$stringBuilder")
//
//            dynamic_time.mText = stringBuilder.toString()
//        }
            mainIvBack.setOnClickAction {
                navTo(0)
            }

            mainBottomNaviSchedule.setOnClickAction {
                navTo(1)
            }

            mainBottomNaviConcentrate.setOnClickAction {
                navTo(2)
            }

            mainBottomNaviCalendar.setOnClickAction {
                navTo(3)
            }

            mainBottomNaviMine.setOnClickAction {
                navTo(4)
            }

            mainBottomNaviAdd.setOnClickAction {
                ARouter.getInstance().build(SCHEDULE_EDIT).navigation()
//            BottomInputDialog(this, "hh", "", "asdf", {}).show()
//            SelectDateDialog(this, "day") { a, b, c -> }.show()
            }

        }

    }

    private fun navTo(item: Int) {
        binding.vpMain.setCurrentItem(item, false)
        if (item != 0) {
            binding.mainIvBack.visibility = View.VISIBLE
        } else {
            binding.mainIvBack.visibility = View.GONE
        }
    }

}