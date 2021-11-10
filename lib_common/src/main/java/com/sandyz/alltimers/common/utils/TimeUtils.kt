package com.sandyz.alltimers.common.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeUtil {
    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    fun timeFormatStr(calendar: Calendar, strDay: String): String {
        var tempStr = ""
        val hour = calendar[Calendar.HOUR_OF_DAY]
        tempStr = if (hour > 11) {
            "下午 $strDay"
        } else {
            "上午 $strDay"
        }
        return tempStr
    }

    /**
     * 时间转化为星期
     *
     * @param indexOfWeek
     * 星期的第几天
     */
    fun getWeekDayStr(indexOfWeek: Int): String {
        var weekDayStr = ""
        when (indexOfWeek) {
            1 -> weekDayStr = "星期日"
            2 -> weekDayStr = "星期一"
            3 -> weekDayStr = "星期二"
            4 -> weekDayStr = "星期三"
            5 -> weekDayStr = "星期四"
            6 -> weekDayStr = "星期五"
            7 -> weekDayStr = "星期六"
        }
        return weekDayStr
    }

    /**
     * 将时间戳格式化，13位的转为10位
     *
     * @param timestamp
     * @return
     */
    fun timestampFormat(timestamp: Long): Long {
        var timestamp = timestamp
        val timestampStr = timestamp.toString() + ""
        if (timestampStr.length == 13) {
            timestamp /= 1000
        }
        return timestamp
    }

    /**
     * 获取日起时间秒差
     *
     * @param time
     * 需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern
     * 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *
     *
     * 如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     * @throws ParseException
     */
    fun formatLongTime(time: String?, pattern: String?): Long {
        var pattern = pattern
        var dTime: Long = 0
        if (time != null) {
            if (pattern == null) pattern = "yyyy-MM-dd HH:mm:ss"
            val tDate: Date?
            try {
                tDate = SimpleDateFormat(pattern).parse(time)
                val today = Date()
                if (tDate != null) {
                    dTime = (today.time - tDate.time) / 1000
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return dTime
    }

    /**
     * 获取日期时间戳
     *
     * @param time
     * 需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern
     * 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *
     *
     * 如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     * @throws ParseException
     */
    fun timeStamp(time: String?, pattern: String?): Long {
        var pattern = pattern
        var dTime: Long = 0
        if (time != null) {
            if (pattern == null) pattern = "yyyy-MM-dd HH:mm:ss"
            val tDate: Date?
            try {
                tDate = SimpleDateFormat(pattern).parse(time)
                if (tDate != null) {
                    dTime = tDate.time / 1000
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return dTime
    }

    fun getTimeStr(timeStamp: Long): String {
        if (timeStamp == 0L) return ""
        val inputTime = Calendar.getInstance()
        inputTime.timeInMillis = timeStamp * 1000
        val currenTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(currenTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return if (calendar.before(inputTime)) {
            "昨天"
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -5)
            if (calendar.before(inputTime)) {
                getWeekDayStr(inputTime[Calendar.DAY_OF_WEEK])
            } else {
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar[Calendar.MONTH] = Calendar.JANUARY
                val year = inputTime[Calendar.YEAR]
                val month = inputTime[Calendar.MONTH] + 1
                val day = inputTime[Calendar.DAY_OF_MONTH]
                "$year/$month/$day"
            }
        }
    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp
     * 单位为秒
     */
    fun getChatTimeStr(timeStamp: Long): String {
        var timeStamp = timeStamp
        if (timeStamp == 0L) return ""
        val inputTime = Calendar.getInstance()
        val timeStr = timeStamp.toString() + ""
        if (timeStr.length == 10) {
            timeStamp = timeStamp * 1000
        }
        inputTime.timeInMillis = timeStamp
        val currentTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        // if (calendar.before(inputTime)){
        // //当前时间在输入时间之前
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy" +
        // "年"+"MM"+"月"+"dd"+"日");
        // return sdf.format(currentTimeZone);
        // }
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("h:mm")
            return timeFormatStr(inputTime, sdf.format(currentTimeZone))
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("h:mm")
            "昨天" + " " + timeFormatStr(inputTime, sdf.format(currentTimeZone))
        } else {
            calendar[Calendar.DAY_OF_MONTH] = 1
            calendar[Calendar.MONTH] = Calendar.JANUARY
            if (calendar.before(inputTime)) {
                val sdf = SimpleDateFormat("M" + "月" + "d" + "日")
                val temp1 = sdf.format(currentTimeZone)
                val sdf1 = SimpleDateFormat("h:mm")
                val temp2 = timeFormatStr(inputTime, sdf1.format(currentTimeZone))
                temp1 + temp2
            } else {
                val sdf = SimpleDateFormat("yyyy" + "/" + "M" + "/" + "d" + " ")
                val temp1 = sdf.format(currentTimeZone)
                val sdf1 = SimpleDateFormat("h:mm")
                val temp2 = timeFormatStr(inputTime, sdf1.format(currentTimeZone))
                temp1 + temp2
            }
        }
    }

    fun monthStr(timeStamp: Long): String {
        val inputTime = Calendar.getInstance()
        var s = ""
        inputTime.timeInMillis = timeStamp * 1000
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy" + " / " + "M" + " / " + "d" + " ")
        if (sdf.format(inputTime.time) == sdf.format(calendar.time)) {
            s = "(今天)"
        }
        return sdf.format(inputTime.time) + s
    }

    fun dayStr(timeStamp: Long): String {
        val inputTime = Calendar.getInstance()
        var s = ""
        inputTime.timeInMillis = timeStamp
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy" + " / " + "M" + " / " + "d" + "  " + "HH:mm")
        if (sdf.format(inputTime.time) == sdf.format(calendar.time)) {
            s = "(今天)"
        }
        return sdf.format(inputTime.time) + s
    }

    /**
     * 群发使用的时间转换
     */
    fun multiSendTimeToStr(timeStamp: Long): String {
        var stamp = timeStamp
        if (stamp == 0L) return ""
        val inputTime = Calendar.getInstance()
        val timeStr = stamp.toString() + ""
        if (timeStr.length == 10) {
            stamp *= 1000
        }
        inputTime.timeInMillis = stamp
        val currentTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(currentTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm")
            "昨天"
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -5)
            if (calendar.before(inputTime)) {
                getWeekDayStr(inputTime[Calendar.DAY_OF_WEEK])
            } else {
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar[Calendar.MONTH] = Calendar.JANUARY
                if (calendar.before(inputTime)) {
                    val sdf = SimpleDateFormat("M" + "/" + "d" + " ")
                    val temp1 = sdf.format(currentTimeZone)
                    val sdf1 = SimpleDateFormat("HH:mm")
                    val temp2 = sdf1.format(currentTimeZone)
                    temp1 + temp2
                } else {
                    val sdf = SimpleDateFormat("yyyy" + "/" + "M" + "/" + "d" + " ")
                    val temp1 = sdf.format(currentTimeZone)
                    val sdf1 = SimpleDateFormat("HH:mm")
                    val temp2 = sdf1.format(currentTimeZone)
                    temp1 + temp2
                }
            }
        }
    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time
     * 需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern
     * 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *
     *
     * 如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    fun formatDisplayTime(time: String?, pattern: String?): String {
        var pattern = pattern
        var display = ""
        val tMin = 60 * 1000
        val tHour = 60 * tMin
        val tDay = 24 * tHour
        if (time != null) {
            if (pattern == null) pattern = "yyyy-MM-dd HH:mm:ss"
            try {
                val tDate = SimpleDateFormat(pattern).parse(time)
                val today = Date()
                val thisYearDf = SimpleDateFormat("yyyy")
                val todayDf = SimpleDateFormat("yyyy-MM-dd")
                val thisYear = Date(thisYearDf.parse(thisYearDf.format(today)).time)
                val yesterday = Date(todayDf.parse(todayDf.format(today)).time)
                val beforeYes = Date(yesterday.time - tDay)
                if (tDate != null) {
                    val halfDf = SimpleDateFormat("MM月dd日")
                    val dTime = today.time - tDate.time
                    display = if (tDate.before(thisYear)) {
                        SimpleDateFormat("yyyy年MM月dd日").format(tDate)
                    } else {
                        if (dTime < tMin) {
                            "刚刚"
                        } else if (dTime < tHour) {
                            Math.ceil((dTime / tMin).toDouble()).toInt().toString() + "分钟前"
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            Math.ceil((dTime / tHour).toDouble()).toInt().toString() + "小时前"
                        } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                            "昨天 " + SimpleDateFormat("HH:mm").format(tDate)
                        } else {
                            multiSendTimeToStr(tDate.time / 1000)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return display
    }

    fun formatDisplayTimestamp(timeStamp: Long): String {
        return formatDisplayTime(
            sf.format(
                Calendar.getInstance().apply { timeInMillis = timeStamp * 1000 }.time
            ), null
        )
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val timeStamp = timeStamp("2021-10-25 18:50:00", null)
        println(multiSendTimeToStr(timeStamp))     //2020/10/27 10:50        星期一                昨天                    10:50
        println(getChatTimeStr(timeStamp))         //2020/10/27 上午 10:50   10月25日下午 6:50     昨天 下午 6:50          上午 10:50 （12小时制）
        println(getTimeStr(timeStamp))             //2020/10/27              星期一                昨天                    10:50
        println(formatDisplayTimestamp(timeStamp)) //2020年10月27日           星期一               昨天 18:50               14分钟前
        println(monthStr(timeStamp))               //2021 / 10 / 25          2021 / 10 / 27 (今天)
    }
}