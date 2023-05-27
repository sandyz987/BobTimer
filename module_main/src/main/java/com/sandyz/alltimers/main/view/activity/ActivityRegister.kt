package com.sandyz.alltimers.main.view.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import com.sandyz.alltimers.common.base.BaseViewModelActivity
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.view.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.main_activity_register.*

class ActivityRegister : BaseViewModelActivity<LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_register)

        main_selector_contractions.isSelect = true

        main_tv_return_login.setOnClickAction {
//            if (checkContractions()) {
//                Config.touristMode = true
//                startActivity(Intent(this, ActivityMain::class.java))
//                finish()
//            }
            finish()
        }

        main_tv_register.setOnClickAction {
            if (checkContractions()) {
                if (main_et_phone.text.toString().length <= 3 || main_et_code.text.toString().length <= 3) {
                    Toast.makeText(this, "账号和密码的长度应大于3~", Toast.LENGTH_SHORT).show()
                    return@setOnClickAction
                }

                viewModel.register(
                    main_et_phone.text.toString(),
                    main_et_code.text.toString()
                ) {
                    onBackPressed()
                }
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