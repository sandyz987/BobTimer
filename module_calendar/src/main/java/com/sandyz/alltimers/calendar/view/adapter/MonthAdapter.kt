package com.sandyz.alltimers.calendar.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.utils.ChinaDate
import com.bigkoo.pickerview.utils.LunarCalendar
import com.sandyz.alltimers.api_schedule.IScheduleService
import com.sandyz.alltimers.api_schedule.ScheduleTimeHelper
import com.sandyz.alltimers.calendar.R
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.api_schedule.ScheduleSortData
import com.sandyz.alltimers.common.utils.*
import kotlinx.android.synthetic.main.calendar_item_day.view.*
import java.util.*

/**
 *@author zhangzhe
 *@date 2022/3/25
 *@description
 */

class MonthAdapter(
    context: Context,
    private val month: DateItem,
    private val selectedDate: MutableLiveData<DateItem>,
    lifeCycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<MonthAdapter.VH>() {

    private val monthDaysLeap = listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val monthDays = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    data class DayBean(
        val day: Int,
        val lunar: String,
        var selected: Boolean = false,
        var isToday: Boolean = false,
        var colorList: MutableList<Int> = mutableListOf()
    )

    private val list = mutableListOf<DayBean>()

    init {
        selectedDate.observe(lifeCycleOwner) { date ->
            list.forEach {
                it.selected = (date.year == month.year && date.month == month.month && date.day == it.day)
            }
            notifyDataSetChanged()
        }
        val leapYear = (month.year % 4 == 0 && month.year % 100 != 0) || month.year % 400 == 0
        val dayCount = if (leapYear) {
            monthDaysLeap[month.month - 1]
        } else {
            monthDays[month.month - 1]
        }
        val weekDay = CalendarUtil.getCalendar(month).apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }.get(Calendar.DAY_OF_WEEK) - 1
        for (i in 0 until weekDay) {
            list.add(DayBean(0, ""))
        }
        for (day in 1..dayCount) {
            val lunar = LunarCalendar.solarToLunar(month.year, month.month, day)
            val lunarDayStr = ChinaDate.getChinaDate(lunar[2])
            if (lunarDayStr == "初一") {
                list.add(DayBean(day, ChinaDate.getMonths(lunar[0])[lunar[1] - 1]))
            } else {
                list.add(DayBean(day, lunarDayStr))
            }
        }
        val today = CalendarUtil.getCalendar().toDateItem()
        if (month.year == today.year && month.month == today.month) {
            list[list.indexOfFirst { it.day == today.day }].isToday = true
            list[list.indexOfFirst { it.day == today.day }].selected = true
        }

        // 将日程标记到每日上
        val scheduleList = ARouter.getInstance().navigation(IScheduleService::class.java).obtainScheduleDataAll()
        scheduleList?.forEach { scheduleData ->
            if (scheduleData.period == "无") {
                val date = CalendarUtil.getDateItem(ScheduleTimeHelper.getNextTarget(scheduleData))
                val color =
                    ResourcesCompat.getDrawable(context.resources, ScheduleSortData.list.find { it.name == scheduleData.sort }?.iconId ?: 0, null)
                        ?.toBitmap(50, 50)?.getPixel(1, 25) ?: Color.parseColor("#FFFFFFFF")
                list.find { it.day == date.day && month.month == date.month && month.year == date.year }?.colorList?.add(color)
            } else {
                var nextDate = ScheduleTimeHelper.getNextTarget(scheduleData)
                while (CalendarUtil.getDateItem(nextDate).year <= month.year && CalendarUtil.getDateItem(nextDate).month <= month.month && nextDate != -1L) {
                    val date = CalendarUtil.getDateItem(nextDate)
                    val color =
                        ResourcesCompat.getDrawable(context.resources, ScheduleSortData.list.find { it.name == scheduleData.sort }?.iconId ?: 0, null)
                            ?.toBitmap(50, 50)?.getPixel(1, 25) ?: Color.parseColor("#FFFFFFFF")
                    list.find { it.day == date.day && month.month == date.month && month.year == date.year }?.colorList?.add(color)
                    nextDate = ScheduleTimeHelper.addPeriod(nextDate, scheduleData)
                }
            }
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvDay = v.calendar_tv_day
        val lunar = v.calendar_tv_lunar
        val cap = v.calendar_view_cap
        val bg = v.calendar_cl_bg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item_day, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (list[position].day == 0) {
            holder.bg.visibility = View.INVISIBLE
        } else {
            holder.bg.visibility = View.VISIBLE
        }
        holder.tvDay.text = list[position].day.toString()
        holder.lunar.text = list[position].lunar
        holder.tvDay.setTextColor(Color.parseColor("#AD7557"))
        holder.lunar.setTextColor(Color.parseColor("#D4B4A2"))
        if (list[position].isToday) {
            if (list[position].selected) {
                holder.bg.setBackgroundResource(R.drawable.calendar_shape_date_today_bg)
                holder.tvDay.setTextColor(Color.parseColor("#FFFFFF"))
                holder.lunar.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                holder.bg.setBackgroundResource(R.drawable.calendar_shape_date_today_unselected_bg)
            }
        } else {
            if (list[position].selected) {
                holder.bg.setBackgroundResource(R.drawable.calendar_shape_date_selected_bg)
            } else {
                holder.bg.setBackgroundResource(0)
            }
        }
        holder.itemView.setOnClickAction {
            selectedDate.value = DateItem(month.year, month.month, list[position].day)
        }
        holder.cap.colorList = list[position].colorList
    }

    override fun getItemCount() = list.size

    fun Long.fixToMidNight(): Long {
        return CalendarUtil.getCalendar(this).apply {
            set(getYear(), getMonth() - 1, getDay(), 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

}