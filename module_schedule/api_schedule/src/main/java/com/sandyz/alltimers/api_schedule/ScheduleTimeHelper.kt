package com.sandyz.alltimers.api_schedule

import com.sandyz.alltimers.common.utils.*
import com.sandyz.alltimers.common.widgets.LogUtils
import java.util.*
import kotlin.math.ceil

/**
 *@author zhangzhe
 *@date 2022/3/23
 *@description
 */

object ScheduleTimeHelper {
    fun getProgress(start: Long, end: Long, now: Long): Float {
        val dayTotal = getDiffDays(start.fixToMidNight(), end)
        val lastDays = getDiffDays(now.fixToMidNight(), end)
        LogUtils.e(
            "progress{{${start.fixToMidNight().asTimeStr()} ${end.asTimeStr()} ${
                now.fixToMidNight().asTimeStr()
            }}} : total:$dayTotal last:$lastDays"
        )
        return (dayTotal - lastDays) / dayTotal.toFloat()
    }

    fun getProgress(scheduleData: ScheduleData): Float {
        if (scheduleData.period.isBlank() || scheduleData.period.startsWith("无")) {
            return getProgress(scheduleData.modifyDate, scheduleData.targetStartDate, CalendarUtil.getCalendar().timeInMillis)
        } else {
            val endDay = getNextTarget(scheduleData)
            val startDay: Long
            when {
                scheduleData.period == "周" -> {
                    startDay = endDay - (1000 * 3600 * 24) * 7
                }
                scheduleData.period.startsWith("间") -> {
                    val daysStr = scheduleData.period.substring(1, scheduleData.period.length)
                    val days = try {
                        daysStr.toInt()
                    } catch (e: Exception) {
                        0
                    } + 1
                    startDay = endDay - (1000 * 3600 * 24) * days
                }
                else -> {
                    when (scheduleData.period) {
                        "年" -> {
                            startDay = CalendarUtil.getDateItem(endDay).apply {
                                year -= 1
                            }.time()
                        }
                        "月" -> {
                            startDay = CalendarUtil.getDateItem(endDay).apply {
                                month -= 1
                            }.time()
                        }
                        else -> {
                            startDay = endDay
                        }
                    }
                }
            }
            LogUtils.d("progress:::::::$startDay $endDay ${CalendarUtil.getCalendar().timeInMillis}")
            return getProgress(startDay, endDay, CalendarUtil.getCalendar().timeInMillis)
        }
    }

    fun getDiffDays(target: Long): Int {
        return getDiffDays(CalendarUtil.getCalendar().timeInMillis, target)
    }

    fun getDiffDays(now: Long, target: Long): Int {
        val targetTime = target.fixToMidNight()
        return ceil((targetTime - now) / (1000 * 3600 * 24f)).toInt()
    }

    fun addPeriod(time: Long, scheduleData: ScheduleData): Long {
        var nextTarget = time
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
                            year += 1
                        }.time()
                    }
                    "月" -> {
                        nextTarget = CalendarUtil.getDateItem(nextTarget).apply {
                            month += 1
                        }.time()
                    }
                    else -> {
                        nextTarget += (1000 * 3600 * 24)
                    }
                }
            }
        }
        return nextTarget
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
            if (nextDay >= 0) {
                return nextTarget
            }
            nextTarget = addPeriod(nextTarget, scheduleData)

        } while (nextDay < 0)
        return nextTarget
    }
}

fun Long.fixToMidNight(): Long {
    return CalendarUtil.getCalendar(this).apply {
        set(getYear(), getMonth() - 1, getDay(), 0, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun Long.asTimeStr(): String {
    return CalendarUtil.getCalendar(this).toTimeString()
}

fun Long.asDateStr(): String {
    return CalendarUtil.getCalendar(this).toDateString()
}


fun main() {
    val cal = CalendarUtil.getCalendar()
    cal.timeInMillis = cal.timeInMillis.fixToMidNight()
    println(ScheduleTimeHelper.getDiffDays(cal.timeInMillis, cal.timeInMillis + 86300000))
}