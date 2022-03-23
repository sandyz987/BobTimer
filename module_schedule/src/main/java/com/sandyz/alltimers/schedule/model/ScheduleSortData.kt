package com.sandyz.alltimers.schedule.model

import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.view.adapter.ScheduleSortAdapter

/**
 *@author zhangzhe
 *@date 2022/3/20
 *@description
 */

object ScheduleSortData {
    val list = mutableListOf(
        ScheduleSortAdapter.SortData("生活", R.drawable.schedule_ic_sort_daily, R.drawable.schedule_ic_sort_daily_item),
        ScheduleSortAdapter.SortData("学习", R.drawable.schedule_ic_sort_learn, R.drawable.schedule_ic_sort_learn_item),
        ScheduleSortAdapter.SortData("工作", R.drawable.schedule_ic_sort_work, R.drawable.schedule_ic_sort_work_item),
        ScheduleSortAdapter.SortData("生日", R.drawable.schedule_ic_sort_birth, R.drawable.schedule_ic_sort_birth_item),
        ScheduleSortAdapter.SortData("节日", R.drawable.schedule_ic_sort_festival, R.drawable.schedule_ic_sort_festival_item),
        ScheduleSortAdapter.SortData("运动", R.drawable.schedule_ic_sort_exercise, R.drawable.schedule_ic_sort_exercise_item),
        ScheduleSortAdapter.SortData("纪念日", R.drawable.schedule_ic_sort_commemoration, R.drawable.schedule_ic_sort_commemoration_item)
    )
}