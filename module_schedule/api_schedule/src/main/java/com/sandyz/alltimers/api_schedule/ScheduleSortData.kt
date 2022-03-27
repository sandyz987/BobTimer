package com.sandyz.alltimers.api_schedule

import com.sandyz.alltimers.common.R

/**
 *@author zhangzhe
 *@date 2022/3/20
 *@description
 */

object ScheduleSortData {
    val list = mutableListOf(
        SortData("生活", R.drawable.schedule_ic_sort_daily, R.drawable.schedule_ic_sort_daily_item),
        SortData("学习", R.drawable.schedule_ic_sort_learn, R.drawable.schedule_ic_sort_learn_item),
        SortData("工作", R.drawable.schedule_ic_sort_work, R.drawable.schedule_ic_sort_work_item),
        SortData("生日", R.drawable.schedule_ic_sort_birth, R.drawable.schedule_ic_sort_birth_item),
        SortData("节日", R.drawable.schedule_ic_sort_festival, R.drawable.schedule_ic_sort_festival_item),
        SortData("运动", R.drawable.schedule_ic_sort_exercise, R.drawable.schedule_ic_sort_exercise_item),
        SortData("纪念日", R.drawable.schedule_ic_sort_commemoration, R.drawable.schedule_ic_sort_commemoration_item)
    )
}