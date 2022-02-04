package com.sandyz.alltimers.common.extensions

import android.graphics.Canvas
import android.graphics.Paint

/**
 *@author zhangzhe
 *@date 2022/2/3
 *@description
 */

fun Canvas.drawTextTop(text: String, x: Float, y: Float, paint: Paint, align: Paint.Align) {
    val metrics = Paint.FontMetrics()
    paint.textAlign = align
    paint.getFontMetrics(metrics)
    val realY = y - metrics.top
    drawText(text, x, realY, paint)
}

fun Canvas.drawTextCenter(text: String, x: Float, y: Float, paint: Paint, align: Paint.Align) {
    val metrics = Paint.FontMetrics()
    paint.textAlign = align
    paint.getFontMetrics(metrics)
    val realY = y + (metrics.bottom - metrics.top) / 2 - metrics.bottom
    drawText(text, x, realY, paint)
}

fun Canvas.drawTextBottom(text: String, x: Float, y: Float, paint: Paint, align: Paint.Align) {
    val metrics = Paint.FontMetrics()
    paint.textAlign = align
    paint.getFontMetrics(metrics)
    val realY = y - metrics.bottom
    drawText(text, x, realY, paint)
}