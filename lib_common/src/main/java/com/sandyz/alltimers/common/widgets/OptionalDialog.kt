package com.sandyz.alltimers.common.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.databinding.CommonDialogChooseBinding
import com.sandyz.alltimers.common.extensions.setOnClickAction

/**
 *@author zhangzhe
 *@date 2020/8/18
 *@description
 */


object OptionalDialog {
    fun show(context: Context, title: String, hideCancel: Boolean = false, onDeny: () -> Unit, onPositive: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.common_transparent_dialog_dark)
        val binding = DataBindingUtil.inflate<CommonDialogChooseBinding>(LayoutInflater.from(context), R.layout.common_dialog_choose, null, false)
        builder.setView(binding.root)
        builder.setCancelable(true)
        binding.tvTipText.text = title
        binding.tvTipDeny.visibility = if (hideCancel) View.GONE else View.VISIBLE
        val dialog = builder.create()
        dialog.show()

        binding.tvTipDeny.setOnClickAction {
            onDeny.invoke()
            dialog.dismiss()
        }
        binding.tvTipPositive.setOnClickAction {
            onPositive.invoke()
            dialog.dismiss()
        }
    }
}