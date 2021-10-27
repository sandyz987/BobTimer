package com.sandyz.lib_common.widgets

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.sandyz.lib_common.R
import kotlinx.android.synthetic.main.common_dialog_choose.view.*

/**
 *@author zhangzhe
 *@date 2020/8/18
 *@description
 */


object OptionalDialog {
    fun show(context: Context, title: String, onDeny: () -> Unit, onPositive: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.common_transparent_dialog)
        val view = LayoutInflater.from(context).inflate(R.layout.common_dialog_choose, null, false)
        builder.setView(view)
        builder.setCancelable(true)
        view.tv_tip_text.text = title

        val dialog = builder.create()
        dialog.show()

        view.tv_tip_deny.setOnClickListener {
            onDeny.invoke()
            dialog.dismiss()
        }
        view.tv_tip_positive.setOnClickListener {
            onPositive.invoke()
            dialog.dismiss()
        }
    }
}