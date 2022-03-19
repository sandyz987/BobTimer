package com.sandyz.alltimers.main.view

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.MAIN_MAIN
import com.sandyz.alltimers.common.config.SCHEDULE_EDIT
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.view.dialog.BottomInputDialog
import com.sandyz.alltimers.common.view.dialog.SelectDateDialog
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.view.adapter.MainAdapter
import kotlinx.android.synthetic.main.main_activity_main.*

@Route(path = MAIN_MAIN)
class ActivityMain : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)
        vp_main.adapter = MainAdapter(this)
        vp_main.setCurrentItem(0, false)
        vp_main.isUserInputEnabled = false
        vp_main.offscreenPageLimit = 5


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
        main_iv_back.setOnClickAction {
            navTo(0)
        }

        main_bottom_navi_schedule.setOnClickAction {
            navTo(1)
        }

        main_bottom_navi_concentrate.setOnClickAction {
            navTo(2)
        }

        main_bottom_navi_calendar.setOnClickAction {
            navTo(3)
        }

        main_bottom_navi_mine.setOnClickAction {
            navTo(4)
        }

        main_bottom_navi_add.setOnClickAction {
            ARouter.getInstance().build(SCHEDULE_EDIT).navigation()
//            BottomInputDialog(this, "hh", "", "asdf", {}).show()
//            SelectDateDialog(this, "day") { a, b, c -> }.show()
        }



    }

    private fun navTo(item: Int) {
        vp_main.setCurrentItem(item, false)
        if (item != 0) {
            main_iv_back.visibility = View.VISIBLE
        } else {
            main_iv_back.visibility = View.GONE
        }
    }

}