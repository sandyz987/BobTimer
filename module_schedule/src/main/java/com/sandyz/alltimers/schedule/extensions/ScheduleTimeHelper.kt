package com.sandyz.alltimers.schedule.extensions

import com.sandyz.alltimers.common.utils.*
import com.sandyz.alltimers.schedule.bean.ScheduleData
import kotlin.math.ceil

/**
 *@author zhangzhe
 *@date 2022/3/23
 *@description
 */

object ScheduleTimeHelper {
    fun getDiffDays(target: Long): Int {
        return getDiffDays(CalendarUtil.getCalendar().timeInMillis, target)
    }

    fun getDiffDays(now: Long, target: Long): Int {
        val targetTime = target.fixToMidNight()
        return ceil((targetTime - now) / (1000 * 3600 * 24f)).toInt() + 1
    }

    // 如果返回-1说明日程过期
    fun getNextTarget(scheduleData: ScheduleData): Long {
        if (scheduleData.period == "无" || scheduleData.period.isBlank()) {
            if (getDiffDays(scheduleData.targetStartDate) < 0) {
                return -1
            }
        }
        var nextDay: Int
        var nextTarget = scheduleData.targetStartDate
        do {
            nextDay = getDiffDays(nextTarget)
            when {
                scheduleData.period == "周" -> {
                    nextTarget += (1000 * 3600 * 24) * 7
                }
                scheduleData.period.startsWith("间") -> {
                    val daysStr = scheduleData.period.substring(1, scheduleData.period.length)
                    val days = try {
                        daysStr.toInt()
                    } catch (e: Exception) {
                        0
                    } + 1
                    nextTarget += (1000 * 3600 * 24) * days
                }
                else -> {
                    when (scheduleData.period) {
                        "年" -> {
                            nextTarget = CalendarUtil.getDateItem(nextTarget).apply {
                                year + 1
                            }.time()
                        }
                        "月" -> {
                            nextTarget = CalendarUtil.getDateItem(nextTarget).apply {
                                month + 1
                            }.time()
                        }
                        else -> {
                            nextTarget += (1000 * 3600 * 24)
                        }
                    }
                }
            }

        } while (nextDay < 0)
        return nextTarget
    }
}

fun Long.fixToMidNight(): Long {
    return CalendarUtil.getCalendar(this).apply {
        set(getYear(), getMonth(), getDay(), 0, 0, 0)
    }.timeInMillis
}