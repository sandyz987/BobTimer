package com.sandyz.alltimers.common.widgets

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Scroller
import androidx.core.view.children
import com.sandyz.alltimers.common.extensions.getScreenWidth
import kotlin.math.abs

class ScrollBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    // 要显示的图片宽高比
    private var mSizeFix = 1f

    // 背景的超大图片框
    private var mImageView: ImageView = ImageView(context).also { addView(it) }

    // 背景的图片
    private var mBitmap: Bitmap? = null

    // 控件的大小
    private var mWidth = 0
    private var mHeight = 0

    // 点击事件原始坐标，用于判断是拖动还是点按
    private var originX = 0f
    private var originY = 0f

    // ImageView原始的坐标，用于拖动时修改图片框位置
    private var picOriginX = 0f
    private var picOriginY = 0f

    // 用于拖动惯性
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val mScroller by lazy { Scroller(context) }

    // 惯性计算器
    private val velocityTracker by lazy { VelocityTracker.obtain() }

    // 是否在拖动背景
    private var isScrolling = false

    // 是否在惯性滑动
    private var isFling = false

    // 上次修改背景的位置，用于计算deltaX坐标的偏移量，根据这个偏移量去更新子view的位置
    private var lastPos = 0

    /**
     * 设置要显示的背景，注意，请自行压缩
     */
    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        // 下一帧直到测量完毕，再调用refresh，刷新显示，否则宽高数值是没有的。
        requestLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h
        mWidth = w
        refresh()
    }

    /**
     * 刷新显示图片
     */
    private fun refresh() {
        // 获取图片长宽比
        val bitmap = mBitmap ?: return
        mSizeFix = bitmap.width.toFloat() / bitmap.height.toFloat()

        mImageView.setImageBitmap(bitmap)
        mImageView.scaleType = ImageView.ScaleType.FIT_XY
        updateBackgroundPosition(0)
    }

    /**
     * 采用内拦截法，将点击事件穿透到子View决定是否处理，不处理再回到本View中
     */
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                originX = event.rawX
                originY = event.rawY
                picOriginX = mImageView.left.toFloat()
                picOriginY = mImageView.top.toFloat()
                // 停止惯性滑动
                handler.removeCallbacks(mFlingRunnable)
                isFling = false
                return false
            }
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        velocityTracker.addMovement(event)
        velocityTracker.computeCurrentVelocity(1000)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果能来到这里说明子view决定不消费事件
                val dx = event.rawX - originX
                val dy = event.rawY - originY
                // 判断是否是拖动
                if (!isScrolling || abs(dx) > 10f || abs(dy) > 10f) {
                    isScrolling = true
                }
                if (isScrolling) {
                    // 如果是拖动则修改背景位置
                    updateBackgroundPosition((dx + picOriginX).toInt())
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                // 开始惯性滚动
                if (isScrolling) {
                    startFling(-velocityTracker.xVelocity)
                    isScrolling = false
                } else {
                    // 没有拖动，则响应点击事件
                    performClick()
                }
                return true
            }
            else -> {
                return super.onTouchEvent(event)
            }
        }
    }

    override fun performClick(): Boolean {
        // 将子view的长按拖动全部取消
        children.forEach {
            if (it is ScrollImageView) {
                if (it.shouldScrollWithBackground) {
                    it.isScrolling = false
                }
            }
        }
        return super.performClick()
    }

    /**
     * 修改大背景的位置
     */
    private fun updateBackgroundPosition(position: Int) {
        val layoutParams = LayoutParams((mSizeFix * mHeight).toInt(), mHeight)
        var pos = position
        // 边界判断
        if (pos > 0) {
            pos = 0
        }
        if (pos < -(mSizeFix * mHeight - context.getScreenWidth()).toInt()) {
            pos = -((mSizeFix * mHeight - context.getScreenWidth())).toInt()
        }
        layoutParams.leftMargin = pos
        mImageView.layoutParams = layoutParams
        // 同时，修改自己的子View的位置
        updateChildrenPosition(pos)
        requestLayout()
    }

    /**
     * 修改子view的位置为随着大背景拖动
     */
    private fun updateChildrenPosition(pos: Int) {
        val dx = pos - lastPos
        children.forEach {
            if (it is ScrollImageView) {
                val layoutParams = it.layoutParams as LayoutParams
                layoutParams.leftMargin += dx
            }
        }
        lastPos = pos
    }

    private val mFlingRunnable = object : Runnable {
        override fun run() {
            if (isFling && (mScroller.computeScrollOffset()) && mScroller.currVelocity > 500f) {
                updateBackgroundPosition(-mScroller.currX)
                mHandler.post(this)
            } else {
                isFling = false
            }
        }
    }

    private fun startFling(vx: Float) {
        isFling = true
        mHandler.removeCallbacks(mFlingRunnable)
        mScroller.fling(
            -(mImageView.layoutParams as LayoutParams).leftMargin,
            0,
            vx.toInt(),
            0,
            0,
            ((mSizeFix * mHeight - context.getScreenWidth()).toInt()),
            0,
            0
        )
        mHandler.post(mFlingRunnable)
    }
}