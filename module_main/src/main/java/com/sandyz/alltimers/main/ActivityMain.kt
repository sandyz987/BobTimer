package com.sandyz.alltimers.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.sandyz.alltimers.common.config.MAIN_MAIN
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.extensions.getScreenHeight
import com.sandyz.alltimers.common.utils.LoadBitmapUtils
import com.sandyz.alltimers.common.widgets.backgroundscroll.ScrollFrameLayout
import com.sandyz.alltimers.common.widgets.backgroundscroll.addWidget
import com.sandyz.alltimers.common.widgets.backgroundscroll.fromSerializationData
import com.sandyz.alltimers.common.widgets.backgroundscroll.saveSerializationData
import com.sandyz.alltimers.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.main_activity_main.*

@Route(path = MAIN_MAIN)
class ActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)
        vp_main.adapter = MainAdapter(this)
        btn_navi.setOnClickListener {
            vp_main.currentItem = 0
        }
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
        LoadBitmapUtils.decodeBitmapFromResourceByHeight(resources, R.drawable.common_ic_background_test, getScreenHeight())?.let {
            dynamic_time.setBitmap(
                it
            )
        }
        dynamic_time.scrollToPercent(0.5f)
        dynamic_time.fromSerializationData()
        dynamic_time.onBind()
    }

    override fun onDestroy() {
        dynamic_time.saveSerializationData()
        super.onDestroy()
    }
}