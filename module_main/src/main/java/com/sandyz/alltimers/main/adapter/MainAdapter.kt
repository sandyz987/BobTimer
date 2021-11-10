package com.sandyz.alltimers.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sandyz.alltimers.common.service.ServiceManager
import com.sandyz.alltimers.common.config.ABSORBED_ENTRY
import com.sandyz.alltimers.common.config.CALENDAR_ENTRY
import com.sandyz.alltimers.common.config.MINE_ENTRY
import com.sandyz.alltimers.common.config.SCHEDULE_ENTRY

class MainAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ServiceManager.getService(SCHEDULE_ENTRY)
            1 -> ServiceManager.getService(ABSORBED_ENTRY)
            2 -> ServiceManager.getService(CALENDAR_ENTRY)
            else -> ServiceManager.getService(MINE_ENTRY)
        }
    }
}