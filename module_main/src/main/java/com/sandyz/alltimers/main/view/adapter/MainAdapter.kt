package com.sandyz.alltimers.main.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sandyz.alltimers.common.config.*
import com.sandyz.alltimers.common.service.ServiceManager

class MainAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ServiceManager.getService(SCHEDULE_ENTRY)
            1 -> ServiceManager.getService(CONCENTRATE_ENTRY)
            2 -> ServiceManager.getService(HOME_ENTRY)
            3 -> ServiceManager.getService(CALENDAR_ENTRY)
            4 -> ServiceManager.getService(MINE_ENTRY)
            else -> ServiceManager.getService(SCHEDULE_ENTRY)
        }
    }
}