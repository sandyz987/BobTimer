package com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.AnticipateOvershootInterpolator
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.DynamicDisplayView

/**
 *@author zhangzhe
 *@date 2022/2/3
 *@description
 */

class OvershootDrawerImpl : DynamicDisplayView.Drawer {
    private val drawablePaint by lazy { Paint() }
    override fun drawBitmap(canvas: Canvas?, fraction: Float, originBitmap: Bitmap?, targetBitmap: Bitmap?, h: Int) {
        val tmpAlpha = when {
            fraction < 0 -> 0f
            fraction > 1 -> 1f
            else -> fraction
        }
        originBitmap?.let {
            drawablePaint.alpha = (255 - tmpAlpha * 255).toInt()
            canvas?.save()
            canvas?.translate(0f, -h * fraction)
            canvas?.drawBitmap(it, 0f, 0f, drawablePaint)
            canvas?.restore()
        }
        targetBitmap?.let {
            drawablePaint.alpha = (tmpAlpha * 255).toInt()
            canvas?.save()
            canvas?.translate(0f, (-h * fraction + h))
            canvas?.drawBitmap(it, 0f, 0f, drawablePaint)
            canvas?.restore()
        }
    }

    override fun getInterpolator() = AnticipateOvershootInterpolator()
    override fun getDuration() = 600L
}