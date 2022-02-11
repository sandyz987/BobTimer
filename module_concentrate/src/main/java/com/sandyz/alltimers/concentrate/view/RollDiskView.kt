package com.sandyz.alltimers.concentrate.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Scroller
import androidx.annotation.DrawableRes
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.utils.BitmapLoader
import com.sandyz.alltimers.concentrate.R
import kotlin.math.pow
import kotlin.math.sqrt

class RollDiskView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mHeight = 0
    private var mWidth = 0
    private var minutes = 0
        set(value) {
            if (value >= 0) {
                if (value / 60 != 0) {
                    hours += value / 60
                }
                field = value % 60
            } else {
                if (-(value - 59) / 60 != 0) {
                    hours -= -(value - 59) / 60
                }
                field = value % 60 + 60
            }
            onTimeChange?.invoke(hours, minutes)
        }
    private var hours = 0
        set(value) {
            field = value
        }
    var onTimeChange: ((Int, Int) -> Unit)? = null

    private val pointerBitmap: Bitmap? =
        BitmapLoader.decodeBitmapFromResourceByWidth(resources, R.drawable.concentrate_ic_roll_pointer, context.dp2px(64))


    private var currentDegreeFixed = 0f
    private var currentDegree = 0f
        set(value) {
            field = value
            // 计算最近的角度参照
            val targetDegree = currentDegree - (currentDegree % 10) - if ((currentDegree % 10 >= 0)) 0 else 10

            if (currentDegree <= currentDegreeFixed - 10) {
                minutes += 5
                currentDegreeFixed = targetDegree + 10
            } else if (currentDegree >= currentDegreeFixed + 10) {
                minutes -= 5
                currentDegreeFixed = targetDegree
            }
        }

    private var lastDegrees = 0f
    private var diskImage: Bitmap? = null
    private var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
        color = resources.getColor(R.color.concentrate_color_roll_disk, null)
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
                lastDegrees = currentDegree
                handler.removeCallbacks(mFlingRunnable)
                isFling = false
                true
            }

            MotionEvent.ACTION_MOVE -> {

                val dx = originX - event.x
                currentDegree =
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

        // 画圆盘
        canvas?.save()
        canvas?.rotate(currentDegree)
        canvas?.translate(-mWidth.toFloat(), -mWidth.toFloat())
        diskImage?.let { canvas?.drawBitmap(it, 0f, 0f, paint) }
        canvas?.restore()

        // 画刻度
        var r = mWidth.toFloat() / 1.414f - context.dp2px(32)
        for (degree in 5 until 365 step 10) {
            canvas?.save()
            canvas?.rotate(degree.toFloat() + currentDegree)
            canvas?.translate(-r, -r)
            canvas?.drawLine(0f, 0f, context.dp2px(12).toFloat(), context.dp2px(12).toFloat(), paint)
            canvas?.restore()
        }

        // 画当前选定的角度
        canvas?.save()
        canvas?.rotate(-currentDegreeFixed + currentDegree + 45)
        r = mWidth.toFloat() / 1.414f
        canvas?.translate(-r, -r)
//        canvas?.drawLine(0f, 0f, context.dp2px(16).toFloat(), context.dp2px(16).toFloat(), paint)
        canvas?.rotate(-45f)
        pointerBitmap?.let {
            canvas?.translate(-it.width / 2f, context.dp2px(-24).toFloat())
            canvas?.drawBitmap(it, 0f, 0f, paint)
        }
        canvas?.restore()





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
                currentDegree = lastDegrees + mScroller.currX
                invalidate()
                handler.post(this)
            } else {
                isFling = false
                fixPosition()
            }
        }
    }

    private var anim: ValueAnimator? = null

    // 圆盘回弹
    private fun fixPosition() {
        anim?.cancel()
        val targetDegree = if ((currentDegree % 10) >= 0) {
            if ((currentDegree % 10) < 5) {
                currentDegree - (currentDegree % 10)
            } else {
                currentDegree - (currentDegree % 10) + 10
            }
        } else {
            if (-(currentDegree % 10) < 5) {
                currentDegree - (currentDegree % 10)
            } else {
                currentDegree - (currentDegree % 10) - 10
            }
        }
        Log.e("sandyzhang", "%${(currentDegree % 10)}  cur:$currentDegree target:$targetDegree")
        anim = ValueAnimator.ofFloat(currentDegree, targetDegree).apply {
            duration = 200L
            interpolator = OvershootInterpolator()
            addUpdateListener {
                currentDegree = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    // 惯性滚动
    private fun startFling(vx: Float) {
        lastDegrees = currentDegree
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