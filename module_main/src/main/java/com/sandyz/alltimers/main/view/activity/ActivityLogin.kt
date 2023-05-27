package com.sandyz.alltimers.main.view.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseViewModelActivity
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.config.MAIN_LOGIN
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.ProgressShowDialog
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.view.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.main_activity_login.*

@Route(path = MAIN_LOGIN)
class ActivityLogin : BaseViewModelActivity<LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_login)

        val usrPair = viewModel.getUserInfo()
        main_et_phone.setText(usrPair.first)
        main_et_code.setText(usrPair.second)

        main_tv_guide_mode.setOnClickAction {
            startActivity(Intent(this, ActivityRegister::class.java))
//            if (checkContractions()) {
//                Config.touristMode = true
//                startActivity(Intent(this, ActivityMain::class.java))
//                finish()
//            }
        }
        main_tv_login.setOnClickAction {
            if (checkContractions()) {
                ProgressShowDialog.show(this, "提示", "正在登录中", false)
                Config.userId = main_et_phone.text.toString()
                Config.password = main_et_code.text.toString()
                viewModel.checkLogin(this, {
                    ProgressShowDialog.hide()
                    toast("登录成功！欢迎回来！")
                    this@ActivityLogin.finish()
                    val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                    startActivity(intent)
                }, {
                    ProgressShowDialog.hide()
                    toast("登录失败，请检查用户名或密码")
                })
            }
        }
    }

    private fun checkContractions(): Boolean {
        if (!main_selector_contractions.isSelect) {
            ObjectAnimator.ofFloat(main_selector_contractions, "translationX", 0f, 20f, 0f, -20f, 0f, 20f, 0f, -20f, 0f).setDuration(500L).start()
            toast("请先同意《隐私条款》哦~")
        }
        return main_selector_contractions.isSelect
    }
}