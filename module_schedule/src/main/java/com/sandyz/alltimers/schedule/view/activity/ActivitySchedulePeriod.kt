package com.sandyz.alltimers.schedule.view.activity

import android.content.Intent
import android.os.Bundle
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.schedule.R

class ActivitySchedulePeriod : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_period)

        val periodStr = intent.getStringExtra("period_str") ?: "无"


    }

    private fun periodEditFinish(periodStr: String) {
        setResult(RESULT_OK, Intent().apply {
            putExtra("period_str", periodStr)
        })
        finish()
    }
}