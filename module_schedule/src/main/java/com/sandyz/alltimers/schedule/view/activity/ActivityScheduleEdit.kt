package com.sandyz.alltimers.schedule.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.api_schedule.fixToMidNight
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
import com.sandyz.alltimers.api_schedule.ScheduleData
import com.sandyz.alltimers.schedule.model.ScheduleReader
import com.sandyz.alltimers.api_schedule.ScheduleSortData
import com.sandyz.alltimers.schedule.databinding.ScheduleActivityScheduleEditBinding
import com.sandyz.alltimers.schedule.view.adapter.ScheduleSortAdapter
import com.sandyz.alltimers.schedule.widget.PeriodHelper
import com.sandyz.alltimers.schedule.widget.RemindHelper
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

@Route(path = SCHEDULE_EDIT)
class ActivityScheduleEdit : BaseActivity() {

    private var adapter: ScheduleSortAdapter? = null
    private var scheduleData: ScheduleData? = null

    private lateinit var binding: ScheduleActivityScheduleEditBinding

    private val sortList = ScheduleSortData.list
    private val schedulePeriodLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            scheduleData?.period = it.data?.getStringExtra("periodStr") ?: "无"
            refresh()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.schedule_activity_schedule_edit)

        binding.apply {
            val id = intent?.getIntExtra("schedule_id", -1)
            scheduleData = id?.let { ScheduleReader.db?.scheduleDao()?.findScheduleData(it) }
            if (scheduleData != null) {
                scheduleTvTitle.text = "编辑日程"
            } else {
                scheduleData = ScheduleData()
                scheduleTvTitle.text = "新建日程"
            }
            refresh()

            scheduleRvSort.layoutManager = GridLayoutManager(this@ActivityScheduleEdit, 2, RecyclerView.HORIZONTAL, false)
            scheduleRvSort.adapter = adapter
            OverScrollDecoratorHelper.setUpOverScroll(scheduleRvSort, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


            scheduleClTargetDate.setOnClickAction {
                SelectDateDialog(this@ActivityScheduleEdit, "day") { y, m, d ->
                    scheduleData?.targetStartDate = CalendarUtil.getCalendar(y, m, d).timeInMillis.fixToMidNight()
                    scheduleData?.modifyDate = CalendarUtil.getCalendar().timeInMillis.fixToMidNight()
                    refresh()
                }.show()
            }

            scheduleClPeriod.setOnClickAction {
                schedulePeriodLauncher.launch(Intent(this@ActivityScheduleEdit, ActivitySchedulePeriod::class.java).apply {
                    putExtra("periodStr", scheduleData?.period)
                })
            }

            scheduleClRemind.setOnClickAction { }

            scheduleClRemarks.setOnClickAction {
                BottomInputDialog(this@ActivityScheduleEdit, "备注", scheduleData?.remarks ?: "", "备注") {
                    scheduleData?.remarks = it
                    refresh()
                }.show()
            }

            scheduleSwTopping.setOnCheckedChangeListener { _, isChecked ->
                scheduleData?.topping = isChecked
            }

            scheduleSwShowProgress.setOnCheckedChangeListener { _, isChecked ->
                scheduleData?.showProgress = isChecked
            }

            scheduleEtName.doOnTextChanged { text, _, _, _ ->
                scheduleData?.name = text.toString()
            }



            scheduleIvBack.setOnClickAction {
                OptionalDialog.show(this@ActivityScheduleEdit, "确定要返回吗？编辑将不会保存~", onDeny = {}) {
                    finish()
                }
            }
            scheduleTvDone.setOnClickAction {
                if (scheduleData?.name?.isNotBlank() != true) {
                    OptionalDialog.show(this@ActivityScheduleEdit, "事件名称不能为空哦~", hideCancel = true, {}) {}
                    return@setOnClickAction
                }
                scheduleData?.sort = adapter?.getSelected() ?: ""
                ScheduleReader.db?.scheduleDao()?.insert(scheduleData)
                Toast.makeText(BaseApp.context, "已保存~", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    private fun refresh() {
        binding.apply {
            scheduleData?.let { scheduleData ->
                if (adapter == null) {
                    adapter = ScheduleSortAdapter(scheduleRvSort, sortList, sortList.indexOfFirst { scheduleData.sort == it.name })
                }

                scheduleEtName.setText(scheduleData.name)
                scheduleTvTargetDate.text = TimeUtil.monthStrWithWeek(scheduleData.targetStartDate)

                scheduleTvPeriod.text = PeriodHelper.getDescription(scheduleData.period)
                scheduleTvRemind.text = RemindHelper.getDescription(scheduleData.remind)

                scheduleTvRemarks.text = scheduleData.remarks
                scheduleTvPic.text = if (scheduleData.backgroundImageUri.isNotBlank()) "有" else "无"
                scheduleSwTopping.isChecked = scheduleData.topping
                scheduleSwShowProgress.isChecked = scheduleData.showProgress
            }
        }

    }


}