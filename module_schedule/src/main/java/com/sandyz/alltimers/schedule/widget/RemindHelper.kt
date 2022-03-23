package com.sandyz.alltimers.schedule.widget

/**
 *@author zhangzhe
 *@date 2022/3/20
 *@description
 */

object RemindHelper {
    fun getDescription(remindStr: String): String {
        if (remindStr == "") return "当天"
        return "提前${remindStr}天"
    }
}