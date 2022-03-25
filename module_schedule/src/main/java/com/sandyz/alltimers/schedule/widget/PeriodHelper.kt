package com.sandyz.alltimers.schedule.widget

import com.sandyz.alltimers.schedule.bean.ScheduleData
import com.sandyz.alltimers.schedule.extensions.ScheduleTimeHelper
import com.sandyz.alltimers.schedule.extensions.asDateStr
import com.sandyz.alltimers.schedule.extensions.asTimeStr

/**
 *@author zhangzhe
 *@date 2022/3/20
 *@description
 */

object PeriodHelper {
    fun getDescription(periodStr: String): String {
        return when (periodStr[0]) {
            '无' -> "不重复"
            '年' -> "每年的当日"
            '间' -> if (periodStr.substring(1, periodStr.length) == "0") {
                "每天"
            } else {
                "每隔" + periodStr.substring(1, periodStr.length) + "天"
            }
            '月' -> "每月重复" // TimeUtil.monthStrWithWeek(targetStartDate) + "后的每月" + periodStr.substring(1, periodStr.length) + "号"
            '周' -> "每周重复" // TimeUtil.monthStrWithWeek(targetStartDate) + "后的每周星期" + periodStr.substring(1, periodStr.length)
            else -> "不可用"
        }
    }


    fun getDescription(scheduleData: ScheduleData): String {
        return  ScheduleTimeHelper.getNextTarget(scheduleData).asDateStr()
    }
}