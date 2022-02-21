package com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer

import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.AnticipateOvershootInterpolator
import com.sandyz.alltimers.common.BaseApp
import com.sandyz.alltimers.common.extensions.drawTextCenter
import com.sandyz.alltimers.common.extensions.sp
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.DynamicDisplayView

/**
 *@author zhangzhe
 *@date 2022/2/3
 *@description
 */

class TimerDrawerImpl : DynamicDisplayView.Drawer {
    private val drawablePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            typeface = BaseApp.typeface
        }
    }

    override fun drawBitmap(view: DynamicDisplayView, canvas: Canvas?, fraction: Float, originalChar: Char, targetChar: Char, h: Int, w: Int) {
        val tmpAlpha = when {
            fraction < 0 -> 0f
            fraction > 1 -> 1f
            else -> fraction
        }

        val originalBitmap = view.getBitmap(originalChar)
        val targetBitmap = view.getBitmap(targetChar)
        drawablePaint.textSize = view.context.sp(view.mTextSize).toFloat()
        drawablePaint.alpha = (255 - tmpAlpha * 255).toInt()
        canvas?.save()
        canvas?.translate(0f, -h * fraction)
        if (originalBitmap != null) {
            canvas?.drawBitmap(originalBitmap, 0f, 0f, drawablePaint)
        } else {
            canvas?.translate(w / 2f, h / 2f)
            canvas?.drawTextCenter(originalChar.toString(), 0f, 0f, drawablePaint, Paint.Align.CENTER)
        }
        canvas?.restore()


        drawablePaint.alpha = (tmpAlpha * 255).toInt()
        canvas?.save()
        canvas?.translate(0f, (-h * fraction + h))
        if (targetBitmap != null) {
            canvas?.drawBitmap(targetBitmap, 0f, 0f, drawablePaint)
        } else {
            canvas?.translate(w / 2f, h / 2f)
            canvas?.drawTextCenter(targetChar.toString(), 0f, 0f, drawablePaint, Paint.Align.CENTER)
        }
        canvas?.restore()


    }

    override fun getInterpolator() = AnticipateOvershootInterpolator()
    override fun getDuration() = 600L
}