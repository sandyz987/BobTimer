package com.sandyz.alltimers.myhome.backgroundscroll

import android.util.Log
import android.widget.FrameLayout

fun ScrollBackgroundView.pasteWidget(
    widgetName: String,
    leftPx: Int,
    topPx: Int,
    rightPx: Int,
    bottomPx: Int,
    drawableId: Int,
    onClickAction: (() -> Unit)?
) {
    post {
        Log.e("sandyzhang", "$mHeight / $mBgHeight")
        val scale = mHeight / mBgHeight.toFloat()
        val width = (rightPx - leftPx) * scale
        val height = (bottomPx - topPx) * scale
        val left = leftPx * scale
        val top = topPx * scale
        Log.e("sandyzhang", "currentwidget: $left / $top / $width / $height")
        Log.e("sandyzhang", "origin: $mBgWidth, $mBgHeight, current: $width, $height")

        removeWidgetByName(widgetName)
        (getWidgetClass("fixed") as? FixedWidget)?.let {
            addView(it.getChildView(this).also { childView ->
                childView.mName = widgetName
                childView.scrollChild = it
                childView.canMove = it.canMove()
                childView.layoutParams = FrameLayout.LayoutParams(width.toInt(), height.toInt())
                childView.shouldScrollWithBackground = it.shouldScrollWithBackground()
                setChildPosition(childView, left.toInt(), top.toInt(), it.shouldScrollWithBackground())
                childView.setOnClickListener {
                    onClickAction?.invoke()
                }
                it.drawableId = drawableId
            })
            onBind()
        }
    }
}