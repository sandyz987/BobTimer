package com.sandyz.alltimers.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Scroller
import kotlin.math.max
import kotlin.math.min

class ScrollBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    // 要显示的图片宽高比
    private var mSizeFix = 1f
    private var iv: ImageView = ImageView(context).also { addView(it) }
    private var mBitmap: Bitmap? = null

    private var mWidth = 0
    private var mHeight = 0

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        refresh()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h
        mWidth = w
        refresh()
    }
    private fun refresh() {

        // 获取图片长宽比
        val bitmap = mBitmap?: return
        mSizeFix = bitmap.width.toFloat() / bitmap.height.toFloat()

        iv.setImageBitmap(bitmap)
        iv.scaleType = ImageView.ScaleType.FIT_XY
        updateBackgroundPosition(0f)

    }

    private var originX = 0f
    private var originY = 0f
    private var picOriginX = 0f
    private var picOriginY = 0f

    private val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val mScroller by lazy {
        Scroller(context)
    }

    private val velocityTracker by lazy { VelocityTracker.obtain() }


    private var isFling = false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        velocityTracker.addMovement(event)
        velocityTracker.computeCurrentVelocity(1000);
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                originX = event.x
                originY = event.y
                picOriginX = iv.left.toFloat()
                picOriginY = iv.top.toFloat()
                handler.removeCallbacks(mFlingRunnable)
                isFling = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - originX
//                val dy = event.y - originY
                updateBackgroundPosition(dx + picOriginX)
                return true
            }
            MotionEvent.ACTION_UP -> {
                startFling(-velocityTracker.xVelocity)
                return true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    private fun updateBackgroundPosition(pos: Float) {
        Log.e("sandyzhang", "updateBackgroundPosition: $pos" )
        val layoutParams = LayoutParams((mSizeFix * mHeight).toInt(), mHeight)
        layoutParams.leftMargin = max(min((pos).toInt(), 0), -(mSizeFix * mHeight - context.getScreenWidth()).toInt())
//                layoutParams.topMargin = (dy + picOriginY).toInt()
        iv.layoutParams = layoutParams
        invalidate()
    }
    private val mFlingRunnable = object: Runnable {
        override fun run() {
            if (isFling && (mScroller.computeScrollOffset()) && mScroller.currVelocity > 500f) {
                updateBackgroundPosition(-mScroller.currX.toFloat())
                mHandler.post(this)
            } else {
                isFling = false
            }
        }
    }

    private fun startFling(vx: Float) {
        isFling = true
        Log.e("sandyzhang", "=========update ${(iv.layoutParams as LayoutParams).leftMargin}")
        mHandler.removeCallbacks(mFlingRunnable)
        mScroller.fling(-(iv.layoutParams as LayoutParams).leftMargin, 0, vx.toInt(), 0, 0, ((mSizeFix * mHeight - context.getScreenWidth()).toInt()), 0, 0)
        mHandler.post(mFlingRunnable)
    }


}