package com.sandyz.alltimers.common.view.dialog

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.KeyboardController
import kotlinx.android.synthetic.main.common_dialog_bottom_sheet_text.view.*

/**
 *@author zhangzhe
 *@date 2022/3/19
 *@description
 */

class BottomInputDialog(
    private val context: Context,
    private val tip: String,
    private val defaultText: String,
    private val hint: String,
    private val onPositive: ((String) -> Unit)
) {
    fun show() {
        val dialog = BottomSheetDialog(context, R.style.common_transparent_dialog)
        val view = LayoutInflater.from(context).inflate(R.layout.common_dialog_bottom_sheet_text, null, false)
        dialog.setContentView(view)
        dialog.show()

        val onDone = {
            KeyboardController.hideInputKeyboard(context, view.common_et_text)
            val inputText = view.common_et_text?.text?.toString() ?: ""
            onPositive.invoke(inputText)
            dialog.dismiss()
        }

        view.common_tv_title.text = tip
        view.common_et_text.hint = hint
        view.common_et_text.setText(defaultText)
        view.common_et_text.setOnEditorActionListener { v, actionId, event ->
            if (actionId == KeyEvent.KEYCODE_ENTER) {
                onDone.invoke()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        view.common_tv_cancel.setOnClickAction {
            KeyboardController.hideInputKeyboard(context, view.common_et_text)
            dialog.dismiss()
        }
        view.common_iv_done.setOnClickAction {
            onDone.invoke()
        }
    }

}