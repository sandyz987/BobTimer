package com.sandyz.alltimers.common.widgets

import android.app.ProgressDialog
import android.content.Context

//转圈圈的弹窗

object ProgressShowDialog {
    private var progressDialog: ProgressDialog? = null

    fun show(context: Context, title: String, message: String, cancelable: Boolean, whenCancel: (() -> Unit)? = null) {
        progressDialog = ProgressDialog.show(context, title, message, true, cancelable)
        progressDialog?.setOnCancelListener {
            if (whenCancel != null) {
                whenCancel()
            }
        }
        progressDialog?.setTitle(title)
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }

    fun hide() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }
}