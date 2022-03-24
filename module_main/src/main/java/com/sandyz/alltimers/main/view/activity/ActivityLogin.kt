package com.sandyz.alltimers.main.view.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.main.R
import kotlinx.android.synthetic.main.main_activity_login.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_login)

        main_tv_guide_mode.setOnClickAction {
            if (checkContractions()) {
                startActivity(Intent(this, ActivityMain::class.java))
                finish()
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