package com.sandyz.alltimers.main.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.sandyz.alltimers.common.base.BaseViewModelActivity
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.view.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.main_activity_splash.*
import kotlin.random.Random

class ActivitySplash : BaseViewModelActivity<LoginViewModel>() {

    private var second = MutableLiveData(4)

    private val runnable = object : Runnable {
        override fun run() {
            second.value = (second.value ?: 0) - 1
            if (second.value == 0) {
                login()
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

        val p = viewModel.getUserInfo()
        Config.userId = p.first
        Config.password = p.second

        main_tv_skip.setOnClickAction {

            mHandler.removeCallbacks(runnable)

            login()
        }

        second.observe(this) {
            main_tv_skip.text = "跳过(${it})"
        }
    }

    private fun login() {
        viewModel.checkLogin(this, {
            toast("登录成功！欢迎回来~")
            startActivity(Intent(this, ActivityMain::class.java))
            finish()
        }, {
            startActivity(Intent(this, ActivityLogin::class.java))
            finish()
        })
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(runnable)
    }
}