package com.sandyz.alltimers.main.view.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseViewModelActivity
import com.sandyz.alltimers.common.config.Config
import com.sandyz.alltimers.common.config.MAIN_LOGIN
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.ProgressShowDialog
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.databinding.MainActivityLoginBinding
import com.sandyz.alltimers.main.view.viewmodel.LoginViewModel

@Route(path = MAIN_LOGIN)
class ActivityLogin : BaseViewModelActivity<LoginViewModel>() {

    private lateinit var binding: MainActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity_login)

        binding.apply {

            val usrPair = viewModel.getUserInfo()
            mainEtPhone.setText(usrPair.first)
            mainEtCode.setText(usrPair.second)

            mainTvGuideMode.setOnClickAction {
                startActivity(Intent(this@ActivityLogin, ActivityRegister::class.java))
//            if (checkContractions()) {
//                Config.touristMode = true
//                startActivity(Intent(this, ActivityMain::class.java))
//                finish()
//            }
            }
            mainTvLogin.setOnClickAction {
                if (checkContractions()) {
                    ProgressShowDialog.show(this@ActivityLogin, "提示", "正在登录中", false)
                    Config.userId = mainEtPhone.text.toString()
                    Config.password = mainEtCode.text.toString()
                    viewModel.checkLogin(this@ActivityLogin, {
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

    }

    private fun checkContractions(): Boolean {
        binding.apply {
            if (!mainSelectorContractions.isSelect) {
                ObjectAnimator.ofFloat(mainSelectorContractions, "translationX", 0f, 20f, 0f, -20f, 0f, 20f, 0f, -20f, 0f).setDuration(500L).start()
                toast("请先同意《隐私条款》哦~")
            }
            return mainSelectorContractions.isSelect
        }
    }
}