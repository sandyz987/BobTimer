package com.sandyz.alltimers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sandyz.lib_common.widgets.OptionalDialog
import com.sandyz.lib_common.widgets.OptionalPopWindow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.setOnClickListener {
//            OptionalDialog.show(this, "是否退出？", {}, {finish()})
            OptionalPopWindow.Builder().with(this).addOptionAndCallback("555") {

            }.addOptionAndCallback("666"){

            }.show(tv)
        }
    }
}