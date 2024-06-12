package com.sandyz.alltimers.common.view.dialog

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.databinding.CommonDialogBottomSheetTextBinding
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.widgets.KeyboardController

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
        val binding = DataBindingUtil.inflate<CommonDialogBottomSheetTextBinding>(LayoutInflater.from(context), R.layout.common_dialog_bottom_sheet_text, null, false)
        dialog.setContentView(binding.root)
        dialog.show()

        val onDone = {
            KeyboardController.hideInputKeyboard(context, binding.commonEtText)
            val inputText = binding.commonEtText.text?.toString() ?: ""
            onPositive.invoke(inputText)
            dialog.dismiss()
        }

        binding.commonTvTitle.text = tip
        binding.commonEtText.hint = hint
        binding.commonEtText.setText(defaultText)
        binding.commonEtText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == KeyEvent.KEYCODE_ENTER) {
                onDone.invoke()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.commonTvCancel.setOnClickAction {
            KeyboardController.hideInputKeyboard(context, binding.commonEtText)
            dialog.dismiss()
        }
        binding.commonIvDone.setOnClickAction {
            onDone.invoke()
        }
    }

}