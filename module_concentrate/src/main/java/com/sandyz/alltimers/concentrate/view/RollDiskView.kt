package com.sandyz.alltimers.concentrate.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Scroller
import androidx.annotation.DrawableRes
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.utils.BitmapLoader
import kotlin.math.pow
import kotlin.math.sqrt

class RollDiskView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mHeight = 0
    private var mWidth = 0
    private var minutes = 0
        set(value) {
            field = value
            onTimeChange?.invoke(hours, minutes)
        }
    private var hours = 0
        set(value) {
            field = value
            onTimeChange?.invoke(hours, minutes)
        }
    var onTimeChange: ((Int, Int) -> Unit)? = null

    private var tmpDegreeAdd = 0f
    private var currentDegrees = 0f
        set(value) {
            field = value
            val targetDegree = if ((currentDegrees % 10) < 5) {
                currentDegrees - (currentDegrees % 10)
            } else {
                currentDegrees - (currentDegrees % 10) + 10
            }

            if (((targetDegree - tmpDegree) < 0 && (targetDegree - currentDegrees) > 0)) {
                minutes += 1
                tmpDegree = value

            } else if (((targetDegree - tmpDegree) > 0 && (targetDegree - currentDegrees) < 0)) {
                minutes -= 1
                tmpDegree = value

            }
//            if (abs(targetDegree - value) < 10 && abs(targetDegree - tmpDegree) > 10) {
//                if (tmpDegree < value) {
//                    minutes -= 1
//                } else {
//                    minutes += 1
//                }
//                tmpDegree = value
//            }

        }

    private var lastDegrees = 0f
    private var diskImage: Bitmap? = null
    private var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
    }

    fun setDiskImageId(@DrawableRes id: Int) {
        post {
            diskImage = BitmapLoader.decodeBitmapFromResourceByWidth(resources, id, mWidth * 2)
            invalidate()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h
        mWidth = w
    }

    private fun getDegree(dx: Float, event: MotionEvent): Float =
        (dx * 360) / (2 * Math.PI * (sqrt((event.x - mWidth / 2).pow(2) + (mWidth - event.y).pow(2)))).toFloat()

    private var originX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        velocityTracker.addMovement(event)
        velocityTracker.computeCurrentVelocity(1000)
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                originX = event.x
                lastDegrees = currentDegrees
                handler.removeCallbacks(mFlingRunnable)
                isFling = false
                true
            }

            MotionEvent.ACTION_MOVE -> {


                val dx = originX - event.x
                currentDegrees =
                    lastDegrees - getDegree(dx, event)
                invalidate()
                true
            }

            MotionEvent.ACTION_UP -> {
                startFling(getDegree(velocityTracker.xVelocity, event))
                true
            }

            else -> super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()

        canvas?.translate(mWidth / 2f, mWidth.toFloat())

        canvas?.save()
        canvas?.rotate(currentDegrees)
        canvas?.translate(-mWidth.toFloat(), -mWidth.toFloat())
        diskImage?.let { canvas?.drawBitmap(it, 0f, 0f, paint) }
        canvas?.restore()

        val r = mWidth.toFloat() / 1.414f - context.dp2px(16)
        for (degree in 5 until 365 step 10) {
            canvas?.save()
            canvas?.rotate(degree.toFloat() + currentDegrees)

            canvas?.translate(-r, -r)
            canvas?.drawLine(0f, 0f, context.dp2px(16).toFloat(), context.dp2px(16).toFloat(), paint)

            canvas?.restore()
        }
        canvas?.restore()


    }


    // 是否在惯性滑动
    private var isFling = false

    // 用于拖动惯性
    private val mScroller by lazy { Scroller(context) }

    // 惯性计算器
    private val velocityTracker by lazy { VelocityTracker.obtain() }


    private val mFlingRunnable = object : Runnable {
        override fun run() {
            if (isFling && (mScroller.computeScrollOffset())) {
                currentDegrees = lastDegrees + mScroller.currX
                invalidate()
                handler.post(this)
            } else {
                isFling = false
                fixPosition()
            }
        }
    }

    private var anim: ValueAnimator? = null
    private fun fixPosition() {
        anim?.cancel()
        (currentDegrees % 10)
        val targetDegree = if ((currentDegrees % 10) < 5) {
            currentDegrees - (currentDegrees % 10)
        } else {
            currentDegrees - (currentDegrees % 10) + 10
        }
        anim = ValueAnimator.ofFloat(currentDegrees, targetDegree).apply {
            duration = 200L
            interpolator = OvershootInterpolator()
            addUpdateListener {
                currentDegrees = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun startFling(vx: Float) {
        lastDegrees = currentDegrees
        isFling = true
        handler.removeCallbacks(mFlingRunnable)
        mScroller.fling(
            0,
            0,
            vx.toInt(),
            0,
            -360 * 5,
            360 * 5,
            0,
            0
        )
        handler.post(mFlingRunnable)
    }

}