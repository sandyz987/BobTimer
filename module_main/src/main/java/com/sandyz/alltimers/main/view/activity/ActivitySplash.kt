package com.sandyz.alltimers.main.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.main.R
import kotlinx.android.synthetic.main.main_activity_splash.*
import kotlin.random.Random

class ActivitySplash : BaseActivity() {

    private var second = MutableLiveData(4)

    private val runnable = object : Runnable {
        override fun run() {
            second.value = (second.value ?: 0) - 1
            if (second.value == 0) {
                startActivity(Intent(this@ActivitySplash, ActivityLogin::class.java))
                finish()
            } else {
                mHandler.postDelayed(this, 1000L)
            }
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_splash)
        main_iv_splash.setImageResource(ResourceGetter.getDrawableId(R.drawable::class.java, "main_splash_${Random.nextInt(1, 6)}"))
        mHandler.postDelayed(runnable, 1000L)

        main_tv_skip.setOnClickAction {
            startActivity(Intent(this, ActivityLogin::class.java))
            mHandler.removeCallbacks(runnable)
            finish()
        }

        second.observe(this) {
            main_tv_skip.text = "跳过(${it})"
        }
    }
}