package com.sandyz.alltimers.schedule.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.SCHEDULE_ENTRY
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.view.activity.ActivityScheduleEdit
import kotlinx.android.synthetic.main.schedule_fragment_schedule.*

@Route(path = SCHEDULE_ENTRY)
class FragmentSchedule : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.schedule_fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schedule_view_bottom_shadow.setOnClickListener {
            startActivity(Intent(context, ActivityScheduleEdit::class.java))
        }
    }


}