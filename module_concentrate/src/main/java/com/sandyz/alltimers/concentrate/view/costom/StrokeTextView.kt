package com.sandyz.alltimers.concentrate.view.costom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.sandyz.alltimers.common.BaseApp


/**
 *@author zhangzhe
 *@date 2022/2/21
 *@description
 */

class StrokeTextView : androidx.appcompat.widget.AppCompatTextView {
    private var outlineTextView: TextView? = null
    private var strokePaint: TextPaint? = null

    constructor(context: Context) : super(context) {
        outlineTextView = TextView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        outlineTextView = TextView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        outlineTextView = TextView(context, attrs, defStyle)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        outlineTextView?.layoutParams = params
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val type = BaseApp.typeface

        //设置轮廓文字
        val outlineText = outlineTextView!!.text
        if (outlineText == null || outlineText != this.text) {
            outlineTextView!!.text = text
            outlineTextView!!.typeface = type
            typeface = type
            postInvalidate()
        }
        outlineTextView!!.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        outlineTextView!!.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        val type = BaseApp.typeface

        if (strokePaint == null) {
            strokePaint = TextPaint()
        }
        //复制原来TextViewg画笔中的一些参数
        val paint = paint
        strokePaint?.textSize = paint.textSize
        strokePaint?.typeface = type
        strokePaint?.flags = paint.flags
        strokePaint?.alpha = paint.alpha

        //自定义描边效果
        strokePaint?.style = Paint.Style.STROKE
        strokePaint?.color = Color.parseColor("#000000")
        strokePaint?.strokeWidth = 4f
        val text = text.toString()

        //在文本底层画出带描边的文本
        strokePaint?.let {
            canvas.drawText(
                text, (width - it.measureText(text)) / 2,
                baseline.toFloat(), it
            )
        }

        super.onDraw(canvas)
    }
}