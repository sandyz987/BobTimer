package com.sandyz.alltimers.schedule.view.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.SCHEDULE_ENTRY
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.bean.ScheduleData
import com.sandyz.alltimers.schedule.view.adapter.ScheduleMainAdapter
import kotlinx.android.synthetic.main.schedule_fragment_schedule.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

@Route(path = SCHEDULE_ENTRY)
class FragmentSchedule : BaseFragment() {

    private val list = mutableListOf(
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true),
        ScheduleData("妈妈生日", "hhh", 129494L, false, "无", "", "", true)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.schedule_fragment_schedule, container, false)
    }

    private val adapter = ScheduleMainAdapter(list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schedule_rv_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        schedule_rv_list.adapter = adapter
        OverScrollDecoratorHelper.setUpOverScroll(schedule_rv_list, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
        schedule_rv_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                when (parent.getChildAdapterPosition(view)) {
                    0 -> {
                        outRect.set(0, view.context.dp2px(20), 0, view.context.dp2px(8))
                    }
                    list.size - 1 -> {
                        outRect.set(0, view.context.dp2px(8), 0, view.context.dp2px(80))
                    }
                    else -> {
                        outRect.set(0, view.context.dp2px(8), 0, view.context.dp2px(8))
                    }
                }
            }
        })

    }


}