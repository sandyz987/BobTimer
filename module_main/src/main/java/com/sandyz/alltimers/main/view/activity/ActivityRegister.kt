package com.sandyz.alltimers.main.view.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sandyz.alltimers.common.base.BaseViewModelActivity
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.main.R
import com.sandyz.alltimers.main.databinding.MainActivityRegisterBinding
import com.sandyz.alltimers.main.view.viewmodel.LoginViewModel

class ActivityRegister : BaseViewModelActivity<LoginViewModel>() {

    private lateinit var binding: MainActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityRegisterBinding>(this, R.layout.main_activity_register).also {
            binding = it
        }

        binding.apply {

            mainSelectorContractions.isSelect = true

            mainTvReturnLogin.setOnClickAction {
//            if (checkContractions()) {
//                Config.touristMode = true
//                startActivity(Intent(this, ActivityMain::class.java))
//                finish()
//            }
                finish()
            }

            mainTvRegister.setOnClickAction {
                if (checkContractions()) {
                    if (mainEtPhone.text.toString().length <= 3 || mainEtCode.text.toString().length <= 3) {
                        Toast.makeText(this@ActivityRegister, "账号和密码的长度应大于3~", Toast.LENGTH_SHORT).show()
                        return@setOnClickAction
                    }

                    viewModel.register(
                        mainEtPhone.text.toString(),
                        mainEtCode.text.toString()
                    ) {
                        onBackPressed()
                    }
                }
            }
        }
    }

    private fun checkContractions(): Boolean {
        if (!binding.mainSelectorContractions.isSelect) {
            ObjectAnimator.ofFloat(binding.mainSelectorContractions, "translationX", 0f, 20f, 0f, -20f, 0f, 20f, 0f, -20f, 0f).setDuration(500L)
                .start()
            toast("请先同意《隐私条款》哦~")
        }
        return binding.mainSelectorContractions.isSelect
    }
}