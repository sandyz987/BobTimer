package com.sandyz.alltimers.schedule.widget

import com.sandyz.alltimers.common.utils.TimeUtil

/**
 *@author zhangzhe
 *@date 2022/3/20
 *@description
 */

object PeriodHelper {
    fun getDescription(targetStartDate: Long, periodStr: String): String {
        return when (periodStr[0]) {
            '无' -> "不重复"
            '年' -> "每年的当日"
            '间' -> "每隔" + periodStr.substring(1, periodStr.length) + "天"
            '月' -> TimeUtil.monthStrWithWeek(targetStartDate) + "后的每月" + periodStr.substring(1, periodStr.length) + "号"
            '周' -> TimeUtil.monthStrWithWeek(targetStartDate) + "后的每周星期" + periodStr.substring(1, periodStr.length)
            else -> "不可用"
        }
    }
}