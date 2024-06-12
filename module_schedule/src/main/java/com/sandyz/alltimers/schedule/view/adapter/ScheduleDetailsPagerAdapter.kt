package com.sandyz.alltimers.schedule.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.sandyz.alltimers.api_schedule.ScheduleTimeHelper
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.TimeUtil
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.api_schedule.ScheduleData
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.widget.PeriodHelper

class ScheduleDetailsPagerAdapter(private val scheduleData: ScheduleData, val layoutList: MutableList<Int>) : PagerAdapter() {
    override fun getCount(): Int = layoutList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = layoutList[position]
        val view = LayoutInflater.from(container.context).inflate(layoutId, container, false)
        container.addView(view)
        // bind
        val nextTarget = ScheduleTimeHelper.getNextTarget(scheduleData)
        view.findViewById<TextView>(R.id.schedule_tv_details_title).text = "距离${scheduleData.name}还有："
        view.findViewById<TextView>(R.id.schedule_tv_details_last_days).text = "${ScheduleTimeHelper.getDiffDays(nextTarget)}天"
        view.findViewById<TextView>(R.id.schedule_tv_details_target_day).text = TimeUtil.monthStrWithWeek(nextTarget)
        view.findViewById<View>(R.id.schedule_iv_details_period_icon).visibility = if (scheduleData.period.startsWith("无")) {
            View.GONE
        } else {
            View.VISIBLE
        }
        view.findViewById<View>(R.id.schedule_iv_details_period_icon).setOnClickAction {
            OptionalDialog.show(view.context, "${PeriodHelper.getDescription(scheduleData.period)}", hideCancel = true, {}) {}
        }
        view.findViewById<TextView>(R.id.schedule_tv_details_ramarks).text = scheduleData.remarks


        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as? View ?: return
        container.removeView(view)
    }
}