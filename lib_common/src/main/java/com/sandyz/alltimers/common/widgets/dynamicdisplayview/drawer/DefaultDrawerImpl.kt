package com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.DynamicDisplayView

/**
 *@author zhangzhe
 *@date 2022/2/3
 *@description
 */

class DefaultDrawerImpl : DynamicDisplayView.Drawer {
    private val drawablePaint by lazy { Paint() }
    override fun drawBitmap(canvas: Canvas?, fraction: Float, originBitmap: Bitmap?, targetBitmap: Bitmap?, h: Int) {
        originBitmap?.let {
            drawablePaint.alpha = (255 - fraction * 255).toInt()
            canvas?.save()
            canvas?.translate(0f, -h * fraction * 0.5f)
            canvas?.drawBitmap(it, 0f, 0f, drawablePaint)
            canvas?.restore()
        }
        targetBitmap?.let {
            drawablePaint.alpha = (fraction * 255).toInt()
            canvas?.save()
            canvas?.translate(0f, (-h * fraction + h) * 0.5f)
            canvas?.drawBitmap(it, 0f, 0f, drawablePaint)
            canvas?.restore()
        }
    }

    override fun getInterpolator() = AccelerateDecelerateInterpolator()
    override fun getDuration() = 600L
}