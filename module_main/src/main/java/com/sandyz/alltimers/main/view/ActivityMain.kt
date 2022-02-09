package com.sandyz.alltimers.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.MAIN_MAIN
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.view.adapter.MainAdapter
import kotlinx.android.synthetic.main.main_activity_main.*

@Route(path = MAIN_MAIN)
class ActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)
        vp_main.adapter = MainAdapter(this)
        vp_main.setCurrentItem(0, false)
        vp_main.isUserInputEnabled = false


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

        bottom_navi_schedule.setOnClickListener {
            vp_main.setCurrentItem(0, false)
        }

        bottom_navi_concentrate.setOnClickListener {
            vp_main.setCurrentItem(1, false)
        }

    }

}