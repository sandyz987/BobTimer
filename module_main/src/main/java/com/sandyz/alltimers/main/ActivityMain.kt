package com.sandyz.alltimers.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.MAIN_MAIN
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
    }
}