package com.sandyz.alltimers.calendar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.calendar.R
import com.sandyz.alltimers.calendar.view.adapter.CalendarMonthPagerAdapter
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.config.CALENDAR_ENTRY
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.CalendarUtil
import com.sandyz.alltimers.common.utils.DateItem
import com.sandyz.alltimers.common.utils.nextMonth
import com.sandyz.alltimers.common.utils.toDateItem
import kotlinx.android.synthetic.main.calendar_fragment_calendar.*
import java.util.*


@Route(path = CALENDAR_ENTRY)
class FragmentCalendar : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val monthList = mutableListOf<DateItem>()
        val cal = CalendarUtil.getCalendar()
        cal.set(Calendar.MONTH, 0)
        monthList.add(cal.toDateItem())
        for (i in 0 until 23) {
            monthList.add(cal.nextMonth(1).toDateItem())
        }
        calendar_vp.adapter = CalendarMonthPagerAdapter(monthList, this)
        calendar_vp.setCurrentItem(CalendarUtil.getCalendar().toDateItem().month - 1, false)
        calendar_tv_month.text = "${CalendarUtil.getCalendar().toDateItem().month}"
        calendar_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                calendar_tv_month.text = "${monthList[position].month}"
                calendar_tv_year.text = "${monthList[position].year}"
                if (position != CalendarUtil.getCalendar().toDateItem().month - 1) {
                    calendar_tv_back_to_current_month.visibility = View.VISIBLE
                } else {
                    calendar_tv_back_to_current_month.visibility = View.GONE
                }
            }
        })
        calendar_tv_back_to_current_month.setOnClickAction {
            calendar_vp.setCurrentItem(CalendarUtil.getCalendar().toDateItem().month - 1, true)
        }

    }

}