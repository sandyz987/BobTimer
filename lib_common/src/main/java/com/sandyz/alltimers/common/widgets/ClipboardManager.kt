package com.sandyz.alltimers.common.widgets

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.sandyz.alltimers.common.extensions.toast

/**
 *@author zhangzhe
 *@date 2021/1/3
 *@description
 */

object ClipboardManager {
    fun copyText(context: Context, content: String) {
        val cm: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("", content)
        cm.setPrimaryClip(mClipData)
        context.toast("已复制到剪切板")
    }

    fun getCopyText(context: Context): String {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        if (cm != null) {
            val data = cm.primaryClip
            if (data != null) {
                val item = data.getItemAt(0)
                if (item != null) {
                    return item.text.toString()
                }
            }
        }
        return ""
    }
}