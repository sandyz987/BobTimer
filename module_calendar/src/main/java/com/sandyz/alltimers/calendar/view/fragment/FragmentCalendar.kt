package com.sandyz.alltimers.calendar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.calendar.databinding.CalendarFragmentCalendarBinding
import com.sandyz.alltimers.calendar.view.adapter.CalendarMonthPagerAdapter
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.CALENDAR_ENTRY
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.CalendarUtil
import com.sandyz.alltimers.common.utils.DateItem
import com.sandyz.alltimers.common.utils.nextMonth
import com.sandyz.alltimers.common.utils.toDateItem
import java.util.Calendar


@Route(path = CALENDAR_ENTRY)
class FragmentCalendar : BaseFragment() {

    private lateinit var binding: CalendarFragmentCalendarBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CalendarFragmentCalendarBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    private val dayOfWeek = listOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    private val monthName = listOf(
        "Jan.",
        "Feb.",
        "Mar.",
        "Apr.",
        "May.",
        "Jun.",
        "Jul.",
        "Aug.",
        "Sept.",
        "Oct.",
        "Nov.",
        "Dec."
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    fun refresh() {
        val monthList = mutableListOf<DateItem>()
        val cal = CalendarUtil.getCalendar()
        cal.add(Calendar.MONTH, -18)
        monthList.add(cal.toDateItem())
        for (i in 0 until 36) {
            monthList.add(cal.nextMonth(1).toDateItem())
        }
        binding.apply {
            calendarVp.adapter = CalendarMonthPagerAdapter(monthList, this@FragmentCalendar).apply {
                selectedDate.observe {
                    calendarTvTimeDay.text = "${it.day}"
                    val weekday = dayOfWeek[CalendarUtil.getCalendar(it).get(Calendar.DAY_OF_WEEK) - 1]
                    calendarTvTimeWeekDay.text = weekday
                    calendarTvTimeMonth.text = "${monthName[it.month - 1]}"
                }
            }
            calendarVp.setCurrentItem(18, false)
            calendarTvMonth.text = "${CalendarUtil.getCalendar().toDateItem().month}"
            calendarVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageSelected(position: Int) {
                    calendarTvMonth.text = "${monthList[position].month}"
                    calendarTvYear.text = "${monthList[position].year}"
                    if (position != 18) {
                        calendarTvBackToCurrentMonth.visibility = View.VISIBLE
                    } else {
                        calendarTvBackToCurrentMonth.visibility = View.GONE
                    }
                }
            })
            calendarTvBackToCurrentMonth.setOnClickAction {
                calendarVp.setCurrentItem(18, true)
            }
        }

    }

}