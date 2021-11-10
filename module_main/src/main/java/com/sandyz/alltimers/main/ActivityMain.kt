package com.sandyz.alltimers.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.MAIN_MAIN
import com.sandyz.alltimers.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.main_activity_main.*
import java.lang.StringBuilder
import kotlin.random.Random

@Route(path = MAIN_MAIN)
class ActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)
        vp_main.adapter = MainAdapter(this)
        btn_navi.setOnClickListener {
            vp_main.currentItem = 0
        }
        dynamic_time.setOnClickListener {
            val r = Random(System.currentTimeMillis())
            val stringBuilder = StringBuilder()
            (0 until 4).forEach { _ ->
                stringBuilder.append(r.nextInt(0,2).toChar() + '0'.toInt())
            }
            Log.e("sandyzhang", "text:$stringBuilder")

            dynamic_time.mText = stringBuilder.toString()
        }
    }
}