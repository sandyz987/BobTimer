package com.sandyz.alltimers.calendar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sandyz.alltimers.calendar.R
import com.sandyz.alltimers.calendar.view.adapter.CalendarMonthPagerAdapter
import com.sandyz.alltimers.common.base.BaseFragment
import com.sandyz.alltimers.common.utils.CalendarUtil
import com.sandyz.alltimers.common.utils.toDateItem
import kotlinx.android.synthetic.main.calendar_fragment_month.*

class FragmentMonth : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        calendar_rv_month.adapter = CalendarMonthPagerAdapter(mutableListOf(CalendarUtil.getCalendar().toDateItem()))
    }

}