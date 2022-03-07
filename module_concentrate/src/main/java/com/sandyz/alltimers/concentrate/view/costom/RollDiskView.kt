package com.sandyz.alltimers.concentrate.view.costom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Scroller
import androidx.core.content.res.ResourcesCompat
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.extensions.drawTextCenter
import com.sandyz.alltimers.common.extensions.sp
import com.sandyz.alltimers.common.utils.BitmapLoader
import com.sandyz.alltimers.concentrate.R
import kotlin.math.*

class RollDiskView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var rollScale = 5
    var currentSeconds = 0
    fun setSeconds(seconds: Int) {
        Log.e("sandyzhangs", "$seconds")
        if (seconds == currentSeconds) return
        if (seconds == currentSeconds + 1) {
            fixPosition(currentDegree - 10)
        }
        if (seconds == currentSeconds - 1) {
            fixPosition(currentDegree + 10)
        }
        currentSeconds = seconds
        invalidate()
    }

    private var mHeight = 0
    private var mWidth = 0
    var isStarted = false
        set(value) {
            field = value
            if (value) {
                // 开始计时
                rollScale = 1
            } else {
                // 停止计时
                rollScale = 5
                currentSeconds = 0
                hours = 0
                minutes = 0
            }
            invalidate()
            onStartEvent?.invoke(value)
        }
    var minutes = 0
        set(value) {
            if (value >= 0) {
                if (value / 60 != 0) {
                    hours += value / 60
                }
                field = value % 60
            } else {

                if (hours >= 1) {
                    if (-(value - 59) / 60 != 0) {
                        hours -= -(value - 59) / 60
                    }
                    field = value % 60 + 60
                } else {
                    field = 0
                    hours = 0
                }
            }

            if (!isStarted && isCountDown) {
                onTimeCountDownSetChange?.invoke(hours, minutes)
            }
        }
    var hours = 0
        set(value) {
            field = min(max(value, 0), 23)
        }
    var onTimeCountDownSetChange: ((Int, Int) -> Unit)? = null
    fun getCountDownTotalSeconds() = hours * 3600 + minutes * 60

    private val pointerBitmap: Bitmap? =
        BitmapLoader.decodeBitmapFromResourceByWidth(resources, R.drawable.concentrate_ic_roll_pointer, context.dp2px(64))


    private var currentDegreeFixed = 0f
    private var currentDegree = 0f
        set(value) {
            field = value
            // 计算最近的角度参照
            val targetDegree = currentDegree - (currentDegree % 10) - if ((currentDegree % 10 >= 0)) 0 else 10

            if (currentDegree <= currentDegreeFixed - 10) {
                minutes += rollScale
                currentDegreeFixed = targetDegree + 10
            } else if (currentDegree >= currentDegreeFixed + 10) {
                minutes -= rollScale
                currentDegreeFixed = targetDegree
            }
            if (isCountDown && !isStarted) {
                currentSeconds = minutes + hours * 60
            }
        }

    private var lastDegrees = 0f
    private var diskImage: Drawable? = null
    private var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
        color = resources.getColor(R.color.concentrate_color_roll_disk)
        textSize = context.sp(12).toFloat()
    }
    var onStartEvent: ((Boolean) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mWidth != w) {
            diskImage = ResourcesCompat.getDrawable(resources, R.drawable.concentrate_ic_roll_disk_pic, null)
            diskImage?.setBounds(0, 0, w * 2, w * 2)
            Log.e("concentrate", "loadImage:$diskImage, resources:$resources, width:$w, id:${R.drawable.concentrate_ic_roll_disk_pic}")
        }
        mHeight = h
        mWidth = w
        invalidate()
    }


    var isCountDown = true
        set(value) {
            field = value
            if (value) {
                minutes = 0
                rollScale = 5
            } else {
                rollScale = 1
            }
            invalidate()
        }

    private fun getDegree(dx: Float, event: MotionEvent): Float =
        (dx * 360) / (2 * Math.PI * (sqrt((event.x - mWidth / 2).pow(2) + (mWidth - event.y).pow(2)))).toFloat()

    private var originX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        velocityTracker.addMovement(event)
        velocityTracker.computeCurrentVelocity(1000)
        if (!isCountDown || isStarted) {
            return false
        }
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
                if (abs(dx) > 5f) {
                    currentDegree =
                        lastDegrees - getDegree(dx, event)
                    invalidate()
                }

                true
            }

            MotionEvent.ACTION_UP -> {
                val dx = originX - event.x
                if (abs(dx) > 5f) {
                    startFling(getDegree(velocityTracker.xVelocity, event))
                    true
                } else {
                    performClick()
                }

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
//        diskImage?.let { canvas?.drawBitmap(it, 0f, 0f, paint) }
        if (canvas != null) {
            diskImage?.draw(canvas)
        }
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

        // 当前角度
        for (degree in -5..90 step 10) {
            val delta = (degree - 45) / 10
            canvas?.save()
            canvas?.rotate(degree.toFloat() + currentDegree - currentDegreeFixed)
            canvas?.translate(-r, -r)
            canvas?.rotate(-45f)
            canvas?.drawTextCenter(getTimeStr(currentSeconds + delta * rollScale), 0f, context.dp2px(32).toFloat(), paint, Paint.Align.CENTER)
            canvas?.restore()
        }


        // 画指针
        canvas?.save()
        canvas?.rotate(45f)
        r = mWidth.toFloat() / 1.414f
        canvas?.translate(-r, -r)
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
                handler?.post(this)
            } else {
                isFling = false
                fixPosition(currentDegree)
            }
        }
    }

    init {
        Log.e("concentrate", "init:$this")

    }

    private var anim: ValueAnimator? = null

    // 圆盘回弹
    private fun fixPosition(degreeToBeFixed: Float) {
        val targetDegree = if ((degreeToBeFixed % 10) >= 0) {
            if ((degreeToBeFixed % 10) < 5) {
                degreeToBeFixed - (degreeToBeFixed % 10)
            } else {
                degreeToBeFixed - (degreeToBeFixed % 10) + 10
            }
        } else {
            if (-(degreeToBeFixed % 10) < 5) {
                degreeToBeFixed - (degreeToBeFixed % 10)
            } else {
                degreeToBeFixed - (degreeToBeFixed % 10) - 10
            }
        }
        Log.e("sandyzhang", "%${(degreeToBeFixed % 10)}  cur:$degreeToBeFixed target:$targetDegree")
        anim?.cancel()
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
        handler?.post(mFlingRunnable)
    }

    companion object {
        fun getTimeStr(s: Int): String {
            if (s < 0) return ""
            val seconds = s % 60
            val minutes = (s / 60) % 60
            val hours = s / 3600
            val secondsStr = if (seconds <= 9) "0$seconds" else "$seconds"
            val hoursStr = if (hours <= 9) "0$hours" else "$hours"
            val minutesStr = if (minutes <= 9) "0$minutes" else "$minutes"
            return if (hours <= 0) {
                "${minutesStr}:${secondsStr}"
            } else {
                "${hoursStr}:${minutesStr}:${secondsStr}"
            }
        }
    }

}