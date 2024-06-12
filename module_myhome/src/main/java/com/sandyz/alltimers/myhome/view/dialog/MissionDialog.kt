package com.sandyz.alltimers.myhome.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.myhome.R

object MissionDialog {
    fun show(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.common_transparent_dialog_dark)
        val view = LayoutInflater.from(context).inflate(R.layout.myhome_dialog_mission, null, false)
        builder.setView(view)
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()

        view.findViewById<View>(R.id.myhome_tv_close).setOnClickAction {
            dialog.dismiss()
        }
    }
}