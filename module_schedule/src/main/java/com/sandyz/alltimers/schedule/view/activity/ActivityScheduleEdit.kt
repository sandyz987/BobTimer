package com.sandyz.alltimers.schedule.view.activity

import android.os.Bundle
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.schedule.R

class ActivityScheduleEdit : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_schedule_edit)

        val id = intent?.getIntExtra("schedule_id", -1)
        id?.let {
            // 当是修改事务

        }



    }

    override fun onPause() {
        super.onPause()

    }


}