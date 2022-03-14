package com.sandyz.alltimers.common.view.costom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.dp2px

class Selector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0

    private val selectDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.common_selector_clicked, null)?.apply {
            setBounds(0, 0, context.dp2px(ICON_SIZE_DP), context.dp2px(ICON_SIZE_DP))
        }
    }
    private val unselectDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.common_selector_no_clicked, null)?.apply {
            setBounds(0, 0, context.dp2px(ICON_SIZE_DP), context.dp2px(ICON_SIZE_DP))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    companion object {
        const val MARGIN_RIGHT_DP = 16
        const val MARGIN_LEFT_DP = 16
        const val ICON_SIZE_DP = 24
    }
}