package com.sandyz.alltimers.common.view.dialog

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.sandyz.alltimers.common.widgets.KeyboardController

@Deprecated("use BottomInputDialog")
class InputDialog(
    private val context: Context,
    private val tip: String,
    private val defaultText: String,
    private val onPositive: ((String) -> Unit)
) {

    fun show() {
        val inputServer = EditText(context)
        inputServer.setText(defaultText)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(tip).setView(inputServer)
            .setNegativeButton("取消", null)
        builder.setPositiveButton(
            "确定"
        ) { _, _ ->
            onPositive.invoke(inputServer.text.toString())
            KeyboardController.hideInputKeyboard(context, inputServer)
        }.show()
    }
}