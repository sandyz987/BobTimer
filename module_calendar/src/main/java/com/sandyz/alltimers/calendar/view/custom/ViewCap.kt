package com.sandyz.alltimers.calendar.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sandyz.alltimers.common.extensions.dp2px

/**
 *@author zhangzhe
 *@date 2022/3/25
 *@description
 */

class ViewCap @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mWidth = 0
    private var mHeight = 0
    private val paint = Paint().apply {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeWidth = context.dp2px(3).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        invalidate()
    }

    var colorList: List<Int> = mutableListOf()
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val margin = context.dp2px(2).toFloat()
        val mid = mHeight / 2f
        val widthPer = (mWidth - 2 * margin) / colorList.size
        var currentX = mWidth - margin
        colorList.reversed().forEach {
            paint.color = it
            canvas?.drawLine(currentX, mid, currentX - widthPer, mid, paint)
            currentX -= widthPer
        }
    }

}