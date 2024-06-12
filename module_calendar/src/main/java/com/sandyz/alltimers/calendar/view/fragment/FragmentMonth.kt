package com.sandyz.alltimers.calendar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sandyz.alltimers.calendar.R
import com.sandyz.alltimers.calendar.databinding.CalendarFragmentMonthBinding
import com.sandyz.alltimers.common.base.BaseFragment

class FragmentMonth : BaseFragment() {

    private lateinit var binding: CalendarFragmentMonthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CalendarFragmentMonthBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        calendar_rv_month.adapter = CalendarMonthPagerAdapter(mutableListOf(CalendarUtil.getCalendar().toDateItem()))
    }

}