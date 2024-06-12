package com.sandyz.alltimers.common.view.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.setOnClickAction
import kotlin.math.max

/**
 *@author zhangzhe
 *@date 2021/6/7
 *@description
 */

data class OptionItem(
    val title: String,
    val data: String
)

class SelectItemDialog(
    private val context: Context,
    private val title: String,
    private val optionList: List<OptionItem>,
    private val defaultSelect: String,
    private val onOptionSelected: ((String) -> Unit)
) {
    private var pvOptions: OptionsPickerView<String>? = null

    fun show() {
        pvOptions =
            OptionsPickerBuilder(context) { opt1, _, _, _ ->
                onOptionSelected.invoke(optionList[opt1].data)
            }.apply {
                setLayoutRes(R.layout.common_dialog_bottom_sheet_option) {
                    it.findViewById<View>(R.id.common_tv_cancel).setOnClickAction {
                        pvOptions?.dismiss()
                    }
                    it.findViewById<View>(R.id.common_iv_done).setOnClickAction {
                        pvOptions?.returnData()
                        pvOptions?.dismiss()
                    }
                    it.findViewById<TextView>(R.id.common_option_tv_title).text = title
                }
                setBgColor(Color.TRANSPARENT)
                setTextColorCenter(Color.parseColor("#995B3A"))
                isDialog(false)
            }.build()

        val list = mutableListOf<String>()
        optionList.forEach { ind -> list.add(ind.title) }
        pvOptions?.setPicker(list)
        val index = max(0, optionList.indexOfFirst { it.data == defaultSelect })
        pvOptions?.setSelectOptions(index)

        pvOptions?.show()

    }
}