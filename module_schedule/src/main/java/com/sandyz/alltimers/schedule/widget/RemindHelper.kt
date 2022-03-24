package com.sandyz.alltimers.schedule.widget

/**
 *@author zhangzhe
 *@date 2022/3/20
 *@description
 */

object RemindHelper {
    fun getDescription(remindStr: String): String {
        if (remindStr.isBlank() || remindStr == "0") return "当天提醒"
        return "提前${remindStr}天提醒"
    }
}