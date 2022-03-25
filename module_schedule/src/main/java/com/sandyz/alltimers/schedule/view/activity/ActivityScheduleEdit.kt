package com.sandyz.alltimers.schedule.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.BaseApp
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SCHEDULE_EDIT
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.CalendarUtil
import com.sandyz.alltimers.common.utils.TimeUtil
import com.sandyz.alltimers.common.view.dialog.BottomInputDialog
import com.sandyz.alltimers.common.view.dialog.SelectDateDialog
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.bean.ScheduleData
import com.sandyz.alltimers.schedule.extensions.fixToMidNight
import com.sandyz.alltimers.schedule.model.ScheduleReader
import com.sandyz.alltimers.schedule.model.ScheduleSortData
import com.sandyz.alltimers.schedule.view.adapter.ScheduleSortAdapter
import com.sandyz.alltimers.schedule.widget.PeriodHelper
import com.sandyz.alltimers.schedule.widget.RemindHelper
import kotlinx.android.synthetic.main.schedule_activity_schedule_edit.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

@Route(path = SCHEDULE_EDIT)
class ActivityScheduleEdit : BaseActivity() {

    private var adapter: ScheduleSortAdapter? = null
    private var scheduleData: ScheduleData? = null

    private val sortList = ScheduleSortData.list
    private val schedulePeriodLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            scheduleData?.period = it.data?.getStringExtra("period_str") ?: "无"
            refresh()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_schedule_edit)

        val id = intent?.getIntExtra("schedule_id", -1)
        scheduleData = id?.let { ScheduleReader.db?.scheduleDao()?.findScheduleData(it) }
        if (scheduleData != null) {
            schedule_tv_title.text = "编辑日程"
        } else {
            scheduleData = ScheduleData()
            schedule_tv_title.text = "新建日程"
        }
        refresh()

        schedule_rv_sort.layoutManager = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        schedule_rv_sort.adapter = adapter
        OverScrollDecoratorHelper.setUpOverScroll(schedule_rv_sort, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


        schedule_cl_target_date.setOnClickAction {
            SelectDateDialog(this, "day") { y, m, d ->
                scheduleData?.targetStartDate = CalendarUtil.getCalendar(y, m, d).timeInMillis.fixToMidNight()
                scheduleData?.modifyDate = CalendarUtil.getCalendar().timeInMillis.fixToMidNight()
                refresh()
            }.show()
        }

        schedule_cl_period.setOnClickAction {
            schedulePeriodLauncher.launch(Intent(this, ActivitySchedulePeriod::class.java).apply {
                putExtra("period_str", scheduleData?.period)
            })
        }

        schedule_cl_remind.setOnClickAction { }

        schedule_cl_remarks.setOnClickAction {
            BottomInputDialog(this, "备注", scheduleData?.remarks ?: "", "备注") {
                scheduleData?.remarks = it
                refresh()
            }.show()
        }

        schedule_sw_topping.setOnCheckedChangeListener { _, isChecked ->
            scheduleData?.topping = isChecked
        }

        schedule_sw_show_progress.setOnCheckedChangeListener { _, isChecked ->
            scheduleData?.showProgress = isChecked
        }

        schedule_et_name.doOnTextChanged { text, _, _, _ ->
            scheduleData?.name = text.toString()
        }



        schedule_iv_back.setOnClickAction {
            OptionalDialog.show(this, "确定要返回吗？编辑将不会保存~", onDeny = {}) {
                finish()
            }
        }
        schedule_tv_done.setOnClickAction {
            if (scheduleData?.name?.isNotBlank() != true) {
                OptionalDialog.show(this, "事件名称不能为空哦~", hideCancel = true, {}) {}
                return@setOnClickAction
            }
            scheduleData?.sort = adapter?.getSelected() ?: ""
            ScheduleReader.db?.scheduleDao()?.insert(scheduleData)
            Toast.makeText(BaseApp.context, "已保存~", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun refresh() {
        scheduleData?.let { scheduleData ->
            if (adapter == null) {
                adapter = ScheduleSortAdapter(schedule_rv_sort, sortList, sortList.indexOfFirst { scheduleData.sort == it.name })
            }

            schedule_et_name.setText(scheduleData.name)
            schedule_tv_target_date.text = TimeUtil.monthStrWithWeek(scheduleData.targetStartDate)

            schedule_tv_period.text = PeriodHelper.getDescription(scheduleData.period)
            schedule_tv_remind.text = RemindHelper.getDescription(scheduleData.remind)

            schedule_tv_remarks.text = scheduleData.remarks
            schedule_tv_pic.text = if (scheduleData.backgroundImageUri.isNotBlank()) "有" else "无"
            schedule_sw_topping.isChecked = scheduleData.topping
            schedule_sw_show_progress.isChecked = scheduleData.showProgress
        }
    }


}