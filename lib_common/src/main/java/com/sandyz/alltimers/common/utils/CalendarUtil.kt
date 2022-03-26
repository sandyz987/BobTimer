package com.sandyz.alltimers.common.utils

import java.text.SimpleDateFormat
import java.util.*

data class DateItem(
        var year: Int = 0, var month: Int = 0, var day: Int = 0
) {
    constructor(calendar: Calendar): this(calendar.getYear(), calendar.getMonth() , calendar.getDay())
    override fun toString(): String {
        return "${year}年${month}月${day}日"
    }
}

object CalendarUtil {

    val sdf = SimpleDateFormat("yyyy-MM-dd")

    fun getCalendar(year: Int, month: Int, day: Int) = getCalendar().apply {
        set(year, month - 1, day)
        setDefaultTime()
    }

    fun getDateItem(): DateItem {
        val cal = getCalendar()
        return DateItem(cal.getYear(), cal.getMonth(), cal.getDay())
    }

    fun getDateItem(time: Long): DateItem {
        val cal = getCalendar().apply {
            timeInMillis = time
        }
        return DateItem(cal.getYear(), cal.getMonth(), cal.getDay())
    }

    fun getCalendar(time: Long): Calendar =
        getCalendar().apply { timeInMillis = time }

    fun getCalendar(dateItem: DateItem): Calendar =
        getCalendar(dateItem.year, dateItem.month, dateItem.day)

    fun getCalendar(): Calendar = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
    }


    fun getMondayOfWeek(week: Int): DateItem {
        val cal = getCalendar()
        cal.set(Calendar.WEEK_OF_YEAR, week)

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return DateItem(cal.getYear(), cal.getMonth(), cal.getDay())
    }

    // 获取第几周
    fun getWeek(): Int {
        val cal = getCalendar()
        cal.firstDayOfWeek = Calendar.MONDAY
        return cal.get(Calendar.WEEK_OF_YEAR)
    }
    // 根据日期获取当前是第几周
    fun getWeek(dateItem: DateItem): Int {
        val cal = getCalendar(dateItem)
        cal.firstDayOfWeek = Calendar.MONDAY
        return cal.get(Calendar.WEEK_OF_YEAR)
    }
    // 获得一年内每个星期的开头
    fun getEveryFirstDayOfWeekByYear(year: Int, firstDayOfWeek: Int = Calendar.MONDAY): List<DateItem> {
        val list = mutableListOf<DateItem>()
        val cal = getCalendar()
        var week = 1
        var inRange = true
        while (inRange) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.WEEK_OF_YEAR, week)
            cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            DateItem(cal).apply {
                if (this.year <= year) {
                    list.add(this)
                } else {
                    inRange = false
                }
            }
            week++
        }
        return list
    }

    // 获得一月内每个星期的开头
    fun getEveryFirstDayOfWeekByMonth(year: Int, month: Int, firstDayOfWeek: Int = Calendar.MONDAY): List<DateItem> {
        val list = mutableListOf<DateItem>()
        val cal = getCalendar()
        var week = 1
        var inRange = true
        while (inRange) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month - 1)
            cal.set(Calendar.WEEK_OF_MONTH, week)
            cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            DateItem(cal).apply {
                if (this.month <= month || (month == 1 && this.month == 12)) {
                    list.add(this)
                } else {
                    inRange = false
                }
            }
            week++
        }
        return list
    }
}

fun main() {
    CalendarUtil.getEveryFirstDayOfWeekByMonth(2021, 1).forEach {
        println(it)
    }
}

fun DateItem.time(): Long {
    val cal = CalendarUtil.getCalendar(this)

    return cal.timeInMillis
}

/**
 * 到本周的星期几
 * @param destination 星期几 （1-7）  星期天是1  星期一是2
 */
fun DateItem.mapDayOfWeek(destination: Int): DateItem {
    val cal = CalendarUtil.getCalendar(this)

    while (cal.get(Calendar.DAY_OF_WEEK) != destination) {
        cal.add(Calendar.DATE, -1)
    }

    return this.apply {
        year = cal.getYear()
        month = cal.getMonth()
        day = cal.getDay()
    }
}

/**
 * 到本月的几号
 * @param destination 几号
 */
fun DateItem.mapDayOfMonth(destination: Int): DateItem {
    val cal = CalendarUtil.getCalendar(this)


    cal.set(Calendar.DATE, destination)

    if (day < destination) {
        cal.add(Calendar.MONTH, -1)
    }

    return this.apply {
        year = cal.getYear()
        month = cal.getMonth()
        day = cal.getDay()
    }
}

/**
 * 本月的天数
 */
fun DateItem.getDayCountOfMonth(): Int {
    return CalendarUtil.getCalendar(this).getActualMaximum(Calendar.DAY_OF_MONTH)
}

/**
 * 本年的天数
 */
fun DateItem.getDayCountOfYear(): Int {
    return CalendarUtil.getCalendar(this).getActualMaximum(Calendar.DAY_OF_YEAR)
}

fun DateItem.nextDay(day: Int) {
    CalendarUtil.getCalendar().nextDay(day).toDateItem()
}

fun DateItem.previousDay(day: Int) {
    CalendarUtil.getCalendar().previousDay(day).toDateItem()
}

fun Calendar.toDateItem(): DateItem {
    return DateItem(getYear(), getMonth(), getDay())
}

fun Calendar.nextDay(day: Int): Calendar {
    return this.apply { add(Calendar.DAY_OF_MONTH, day) }
}

fun Calendar.nextMonth(month: Int): Calendar {
    return this.apply { add(Calendar.MONTH, month) }
}

fun Calendar.previousDay(day: Int): Calendar {
    return this.apply { add(Calendar.DAY_OF_MONTH, - day) }
}

fun Calendar.getMonth(): Int {
    return get(Calendar.MONTH) + 1
}

fun Calendar.getYear(): Int {
    return get(Calendar.YEAR)
}

fun Calendar.getDay(): Int {
    return get(Calendar.DATE)
}

fun Calendar.getHour(): Int {
    return get(Calendar.HOUR_OF_DAY)
}

fun Calendar.getMinute(): Int {
    return get(Calendar.MINUTE)
}

fun Calendar.getSecond(): Int {
    return get(Calendar.SECOND)
}

fun Calendar.setDefaultTime() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
}

fun Calendar.toTimeString(): String {
    return "${getYear()}年${getMonth()}月${getDay()}日 ${getHour()}:${getMinute()}:${getSecond()}"
}

fun Calendar.toDateString(): String {
    return "${getYear()}年${getMonth()}月${getDay()}日"
}

