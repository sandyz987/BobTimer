package com.sandyz.alltimers.schedule.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SCHEDULE_EDIT
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.bean.ScheduleData
import com.sandyz.alltimers.schedule.model.ScheduleReader
import com.sandyz.alltimers.schedule.view.adapter.ScheduleSortAdapter
import kotlinx.android.synthetic.main.schedule_activity_schedule_edit.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

@Route(path = SCHEDULE_EDIT)
class ActivityScheduleEdit : BaseActivity() {

    private lateinit var adapter: ScheduleSortAdapter
    private var scheduleData: ScheduleData? = null

    private val sortList = mutableListOf(
        ScheduleSortAdapter.SortData("生活", R.drawable.schedule_ic_sort_daily),
        ScheduleSortAdapter.SortData("学习", R.drawable.schedule_ic_sort_learn),
        ScheduleSortAdapter.SortData("工作", R.drawable.schedule_ic_sort_work),
        ScheduleSortAdapter.SortData("生日", R.drawable.schedule_ic_sort_birth),
        ScheduleSortAdapter.SortData("节日", R.drawable.schedule_ic_sort_festival),
        ScheduleSortAdapter.SortData("运动", R.drawable.schedule_ic_sort_exercise),
        ScheduleSortAdapter.SortData("纪念日", R.drawable.schedule_ic_sort_commemoration),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_schedule_edit)

        val id = intent?.getIntExtra("schedule_id", -1)
        scheduleData = id?.let { ScheduleReader.db?.counterDao()?.findScheduleData(it) }
        if (scheduleData != null) {
            scheduleData?.let { scheduleData ->
                adapter = ScheduleSortAdapter(schedule_rv_sort, sortList, sortList.indexOfFirst { scheduleData.sort == it.name })
                schedule_et_name.setText(scheduleData.name)
                schedule_tv_remarks.text = scheduleData.remarks
                schedule_tv_remind.text = scheduleData.remindType
            }
        } else {
            adapter = ScheduleSortAdapter(schedule_rv_sort, sortList, 0)
        }
        schedule_rv_sort.layoutManager = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        schedule_rv_sort.adapter = adapter
        OverScrollDecoratorHelper.setUpOverScroll(schedule_rv_sort, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(schedule_rv_sort)



        schedule_iv_back.setOnClickAction {
            OptionalDialog.show(this, "确定要返回吗？编辑将不会保存~", {}) {
                finish()
            }
        }
        schedule_tv_done.setOnClickAction {

        }

    }


}