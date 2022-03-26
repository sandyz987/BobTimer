package com.sandyz.alltimers.calendar.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.sandyz.alltimers.calendar.R
import com.sandyz.alltimers.common.utils.CalendarUtil
import com.sandyz.alltimers.common.utils.DateItem
import com.sandyz.alltimers.common.utils.toDateItem
import kotlinx.android.synthetic.main.calendar_fragment_month.view.*

class CalendarMonthPagerAdapter(private val monthList: MutableList<DateItem>, private val lifecycleOwner: LifecycleOwner) : PagerAdapter() {
    val selectedDate = MutableLiveData(CalendarUtil.getCalendar().toDateItem())

    override fun getCount(): Int = monthList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.calendar_fragment_month, container, false)
        container.addView(view)
        // bind

        view.calendar_rv_month.layoutManager = GridLayoutManager(view.context, 7, RecyclerView.VERTICAL, false)
        view.calendar_rv_month.adapter = MonthAdapter(monthList[position], selectedDate, lifecycleOwner)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as? View ?: return
        container.removeView(view)
    }
}