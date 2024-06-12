package com.sandyz.alltimers.schedule.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.view.dialog.OptionItem
import com.sandyz.alltimers.common.view.dialog.SelectItemDialog
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.databinding.ScheduleActivityPeriodBinding

class ActivitySchedulePeriod : BaseActivity() {
    private val select = MutableLiveData<String>()
    private val periodStr = MutableLiveData<String>()
    private val periodOption = mutableListOf(
        OptionItem("每天", "0"),
        OptionItem("间隔一天", "1"),
        OptionItem("间隔两天", "2"),
        OptionItem("间隔三天", "3"),
        OptionItem("间隔四天", "4"),
        OptionItem("间隔五天", "5"),
        OptionItem("间隔十天", "10"),
        OptionItem("间隔十五天", "15"),
        OptionItem("间隔二十天", "20")
    )

    private lateinit var binding: ScheduleActivityPeriodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.schedule_activity_period)

        binding.apply {


            select.observe(this@ActivitySchedulePeriod) {
                clearSelected()
                when (it) {
                    "无" -> {
                        schedulePeriodSelectorNone.isSelect = true
                        periodStr.value = "无"
                    }

                    "年" -> {
                        schedulePeriodSelectorYear.isSelect = true
                        periodStr.value = "年"
                    }

                    "周" -> {
                        schedulePeriodSelectorWeek.isSelect = true
                        periodStr.value = "周"
                    }

                    "月" -> {
                        schedulePeriodSelectorMonth.isSelect = true
                        periodStr.value = "月"
                    }

                    "间" -> {
                        schedulePeriodSelectorPeriod.isSelect = true
                    }
                }
            }

            periodStr.observe(this@ActivitySchedulePeriod, { p ->
                if (select.value == "间") {
                    schedulePeriodSelectorPeriod.text = (periodOption.findLast { it.data == p.substring(1, p.length) }?.title ?: "") + " >>"
                } else {
                    schedulePeriodSelectorPeriod.text = "间隔多少天一次 >>"
                }
            })

            schedulePeriodSelectorNone.setOnClickAction {
                schedulePeriodSelectorNone.isSelect = !schedulePeriodSelectorNone.isSelect
                select.value = "无"
            }

            schedulePeriodSelectorYear.setOnClickAction {
                schedulePeriodSelectorYear.isSelect = !schedulePeriodSelectorYear.isSelect
                select.value = "年"
            }

            schedulePeriodSelectorMonth.setOnClickAction {
                schedulePeriodSelectorMonth.isSelect = !schedulePeriodSelectorMonth.isSelect
                select.value = "月"
            }

            schedulePeriodSelectorWeek.setOnClickAction {
                schedulePeriodSelectorWeek.isSelect = !schedulePeriodSelectorWeek.isSelect
                select.value = "周"
            }

            schedulePeriodSelectorPeriod.setOnClickAction {
                schedulePeriodSelectorPeriod.isSelect = !schedulePeriodSelectorPeriod.isSelect
                select.value = "间"
                if (periodStr.value?.startsWith("间") != true) {
                    periodStr.value = "间0"
                }

                SelectItemDialog(
                    this@ActivitySchedulePeriod, "请选择周期",
                    periodOption,
                    periodStr.value?.substring(1, periodStr.value?.length ?: 1) ?: "0"
                ) {
                    periodStr.value = "间${it}"
                }.show()
            }

            periodStr.value = intent.getStringExtra("period_str") ?: "无"
            select.value = periodStr.value?.substring(0, 1) ?: "无"

            schedulePeriodIvBack.setOnClickAction {
                finish()
            }

            scheduleTvPeriodDone.setOnClickAction {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("period_str", periodStr.value ?: "无")
                    Log.e("sandyzhang", "finish: ${periodStr.value ?: "无"}")
                })
                finish()
            }
        }

    }

    private fun clearSelected() {
        binding.apply {
            schedulePeriodSelectorPeriod.isSelect = false
            schedulePeriodSelectorNone.isSelect = false
            schedulePeriodSelectorYear.isSelect = false
            schedulePeriodSelectorMonth.isSelect = false
            schedulePeriodSelectorWeek.isSelect = false
        }
    }

    private fun periodEditFinish(periodStr: String) {
        setResult(RESULT_OK, Intent().apply {
            putExtra("period_str", periodStr)
        })
        finish()
    }
}