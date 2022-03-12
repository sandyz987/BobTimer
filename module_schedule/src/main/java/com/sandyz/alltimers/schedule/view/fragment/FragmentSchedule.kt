package com.sandyz.alltimers.schedule.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.SCHEDULE_ENTRY
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.bean.ScheduleData
import com.sandyz.alltimers.schedule.view.adapter.ScheduleMainAdapter
import kotlinx.android.synthetic.main.schedule_fragment_schedule.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

@Route(path = SCHEDULE_ENTRY)
class FragmentSchedule : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.schedule_fragment_schedule, container, false)
    }

    private val adapter = ScheduleMainAdapter(
        mutableListOf(
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", ""),
            ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "")
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schedule_rv_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        schedule_rv_list.adapter = adapter
        OverScrollDecoratorHelper.setUpOverScroll(schedule_rv_list, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)


    }


}