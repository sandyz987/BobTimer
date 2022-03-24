package com.sandyz.alltimers.schedule.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.view.dialog.OptionItem
import com.sandyz.alltimers.common.view.dialog.SelectItemDialog
import com.sandyz.alltimers.schedule.R
import kotlinx.android.synthetic.main.schedule_activity_period.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_period)

        select.observe(this, {
            clearSelected()
            when (it) {
                "无" -> {
                    schedule_period_selector_none.isSelect = true
                    periodStr.value = "无"
                }
                "年" -> {
                    schedule_period_selector_year.isSelect = true
                    periodStr.value = "年"
                }
                "周" -> {
                    schedule_period_selector_week.isSelect = true
                    periodStr.value = "周"
                }
                "月" -> {
                    schedule_period_selector_month.isSelect = true
                    periodStr.value = "月"
                }
                "间" -> {
                    schedule_period_selector_period.isSelect = true
                }
            }
        })

        periodStr.observe(this, { p ->
            if (select.value == "间") {
                schedule_period_selector_period.text = (periodOption.findLast { it.data == p.substring(1, p.length) }?.title ?: "") + " >>"
            } else {
                schedule_period_selector_period.text = "间隔多少天一次 >>"
            }
        })

        schedule_period_selector_none.setOnClickAction {
            schedule_period_selector_none.isSelect = !schedule_period_selector_none.isSelect
            select.value = "无"
        }

        schedule_period_selector_year.setOnClickAction {
            schedule_period_selector_year.isSelect = !schedule_period_selector_year.isSelect
            select.value = "年"
        }

        schedule_period_selector_month.setOnClickAction {
            schedule_period_selector_month.isSelect = !schedule_period_selector_month.isSelect
            select.value = "月"
        }

        schedule_period_selector_week.setOnClickAction {
            schedule_period_selector_week.isSelect = !schedule_period_selector_week.isSelect
            select.value = "周"
        }

        schedule_period_selector_period.setOnClickAction {
            schedule_period_selector_period.isSelect = !schedule_period_selector_period.isSelect
            select.value = "间"
            if (periodStr.value?.startsWith("间") != true) {
                periodStr.value = "间0"
            }

            SelectItemDialog(
                this, "请选择周期",
                periodOption,
                periodStr.value?.substring(1, periodStr.value?.length ?: 1) ?: "0"
            ) {
                periodStr.value = "间${it}"
            }.show()
        }

        periodStr.value = intent.getStringExtra("period_str") ?: "无"
        select.value = periodStr.value?.substring(0, 1) ?: "无"

        schedule_period_iv_back.setOnClickAction {
            finish()
        }

        schedule_tv_period_done.setOnClickAction {
            setResult(RESULT_OK, Intent().apply {
                putExtra("period_str", periodStr.value ?: "无")
                Log.e("sandyzhang","finish: ${periodStr.value ?: "无"}")
            })
            finish()
        }

    }

    private fun clearSelected() {
        schedule_period_selector_period.isSelect = false
        schedule_period_selector_none.isSelect = false
        schedule_period_selector_year.isSelect = false
        schedule_period_selector_month.isSelect = false
        schedule_period_selector_week.isSelect = false
    }

    private fun periodEditFinish(periodStr: String) {
        setResult(RESULT_OK, Intent().apply {
            putExtra("period_str", periodStr)
        })
        finish()
    }
}