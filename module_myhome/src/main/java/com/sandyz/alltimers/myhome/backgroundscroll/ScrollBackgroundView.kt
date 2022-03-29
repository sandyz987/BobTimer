package com.sandyz.alltimers.myhome.backgroundscroll

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Scroller
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sandyz.alltimers.common.extensions.getScreenWidth
import kotlinx.android.synthetic.main.myhome_fragment_home.*
import kotlin.math.abs

class ScrollBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 长按多久可以拖动控件
    var longDragTime = 800L

    // 松开手指后是否立即结束拖动控件
    var endDrayImmediately = false

    // 要显示的图片宽高比
    private var mSizeFix = 1f

    // 背景的超大图片框
    var mBackgroundImage: ImageView = ImageView(context).also { addView(it) }

    // 背景的图片
    private var bgResourceId: Int = 0

    // 控件的大小
    private var mWidth = 0
    private var mHeight = 0

    // 底图的原始像素大小
    var mBgWidth = 0
    var mBgHeight = 0


    // 点击事件原始坐标，用于判断是拖动还是点按
    private var originX = 0f
    private var originY = 0f

    // ImageView原始的坐标，用于拖动时修改图片框位置
    private var picOriginX = 0f
    private var picOriginY = 0f

    // 用于拖动惯性
    private val mScroller by lazy { Scroller(context) }

    // 惯性计算器
    private val velocityTracker by lazy { VelocityTracker.obtain() }

    // 是否在拖动背景
    private var isScrolling = false

    // 是否在惯性滑动
    private var isFling = false

    // 上次修改背景的位置，用于计算deltaX坐标的偏移量，根据这个偏移量去更新子view的位置
    private var lastPos = 0

    init {
        setWillNotDraw(false)
        clipChildren = false
        clipChildren = false
    }

    /**
     * 设置要显示的背景，注意，请自行压缩
     */
//    fun setBitmap(bitmap: Bitmap) {
//        mBitmap = bitmap
//        // 下一帧直到测量完毕，再调用refresh，刷新显示，否则宽高数值是没有的。
//        requestLayout()
//    }

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
//        val bitmap = mBitmap ?: return
        mSizeFix = mBgWidth / mBgHeight.toFloat()

//        mImageView.setImageBitmap(bitmap)
        mBackgroundImage.scaleType = ImageView.ScaleType.FIT_XY
        updateBackgroundPosition(0)
    }

    fun setBgResourceId(@DrawableRes id: Int) {
        bgResourceId = id
        val option = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, id, option)
        mBgWidth = option.outWidth
        mBgHeight = option.outHeight
        Log.e("sandyzhang", "$mBgWidth, $mBgHeight")
        refresh()

        post {
            Log.e("sandyzhang", "iv: ${mBackgroundImage.width}, ${mBackgroundImage.height}")
            Glide.with(context).load(id).into(mBackgroundImage)
        }
    }

    fun setSize(width: Int, height: Int) {
        mBgWidth = width
        mBgHeight = height
        refresh()
    }

    /**
     * 采用内拦截法，将点击事件穿透到子View决定是否处理，不处理再回到本View中
     */
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                originX = event.rawX
                originY = event.rawY
                picOriginX = mBackgroundImage.left.toFloat()
                picOriginY = mBackgroundImage.top.toFloat()
                // 停止惯性滑动
                handler.removeCallbacks(mFlingRunnable)
                smoothScrollAnimator?.let {
                    if (it.isStarted)
                        it.cancel()
                }
                isFling = false
                // 如果下方子view消费，则不再回到本view
                return false
            }
        }
        // 如果下方子view不消费，则返回本view
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
                parent.requestDisallowInterceptTouchEvent(true)
                val dx = event.rawX - originX
                val dy = event.rawY - originY
                // 判断是否是拖动
                if (!isScrolling && (abs(dx) > 10f || abs(dy) > 10f)) {
                    notifyChildMoving(true)
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
                    notifyChildMoving(false)
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
            if (it is ScrollFrameLayout) {
                it.isScrolling = false
            }
        }
        return super.performClick()
    }

    private fun notifyChildMoving(isMoving: Boolean) {
        children.forEach {
            if (it is ScrollFrameLayout) {
                if (isMoving) {
                    it.scrollChild?.startMove(it)
                } else {
                    it.scrollChild?.finishMove(it)
                }
            }
        }
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
        mBackgroundImage.layoutParams = layoutParams
        // 同时，修改自己的子View的位置
        updateChildrenPosition(pos)
        requestLayout()
//        ViewCompat.postOnAnimation()
    }

    /**
     * 修改子view的位置为随着大背景拖动
     */
    private fun updateChildrenPosition(pos: Int) {
        val dx = pos - lastPos
        children.forEach {
            if (it is ScrollFrameLayout && it.shouldScrollWithBackground) {
                val layoutParams = it.layoutParams as LayoutParams
                layoutParams.leftMargin += dx
            }
        }
        lastPos = pos
    }

    /**
     * 设置子view的位置
     */
    fun setChildPosition(
        scrollFrameLayout: ScrollFrameLayout,
        posX: Int,
        posY: Int,
        width: Int,
        height: Int,
        shouldScrollWithBackground: Boolean = true
    ) {
        scrollFrameLayout.post {
            val childLayoutParams = scrollFrameLayout.layoutParams as? LayoutParams ?: return@post
            if (!(width == 0 || height == 0)) {
                childLayoutParams.width = width
                childLayoutParams.height = height
            }
            val ivLayoutParams = mBackgroundImage.layoutParams as LayoutParams
            if (shouldScrollWithBackground) {
                childLayoutParams.leftMargin = posX + ivLayoutParams.leftMargin
                childLayoutParams.topMargin = posY + ivLayoutParams.topMargin
            } else {
                childLayoutParams.leftMargin = posX
                childLayoutParams.topMargin = posY
            }
            scrollFrameLayout.layoutParams = childLayoutParams
            requestLayout()
            onBind()
        }
    }

    /**
     * 设置子view的位置
     */
    fun setChildPosition(widgetName: String, posX: Int, posY: Int, shouldScrollWithBackground: Boolean = true) {
        children.forEach {
            if (it is ScrollFrameLayout && it.getWidgetType() == widgetName) {
                setChildPosition(it, posX, posY, 0, 0, shouldScrollWithBackground)
            }
        }
    }

    /**
     * 获取子view相对于背景的位置，用于保存子view位置
     */
    fun getChildPosition(widgetName: String): Pair<Int, Int>? {
        children.forEach {
            if (it is ScrollFrameLayout && it.getWidgetType() == widgetName) {
                val ivLayoutParams = mBackgroundImage.layoutParams as LayoutParams
                val childLayoutParams = it.layoutParams as LayoutParams
                return if (it.shouldScrollWithBackground) {
                    Pair(
                        -ivLayoutParams.leftMargin + childLayoutParams.leftMargin,
                        -ivLayoutParams.topMargin + childLayoutParams.topMargin
                    )
                } else {
                    Pair(childLayoutParams.leftMargin, childLayoutParams.topMargin)
                }
            }
        }
        return null
    }

    private val mFlingRunnable = object : Runnable {
        override fun run() {
            if (isFling && (mScroller.computeScrollOffset()) && mScroller.currVelocity > 500f) {
                updateBackgroundPosition(-mScroller.currX)
                handler?.post(this)
            } else {
                isFling = false
                notifyChildMoving(false)
            }
        }
    }

    private fun startFling(vx: Float) {
        isFling = true
        handler.removeCallbacks(mFlingRunnable)
        mScroller.fling(
            -(mBackgroundImage.layoutParams as LayoutParams).leftMargin,
            0,
            vx.toInt(),
            0,
            0,
            ((mSizeFix * mHeight - context.getScreenWidth()).toInt()),
            0,
            0
        )
        handler?.post(mFlingRunnable)
    }

    /**
     * 背景滚动到指定位置
     */
    private var smoothScrollAnimator: ValueAnimator? = null
    fun scrollToPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float, smoothly: Boolean = true) {
        post {
            smoothScrollAnimator?.let {
                if (it.isStarted) {
                    it.cancel()
                }
            }
            val maxWidth = mSizeFix * mHeight - context.getScreenWidth()
            if (smoothly) {
                val start = (mBackgroundImage.layoutParams as? LayoutParams?)?.leftMargin ?: return@post
                val end = (-maxWidth * percent).toInt()
                smoothScrollAnimator = ValueAnimator.ofInt(start, end).apply {
                    duration = 600L
                    addUpdateListener {
                        updateBackgroundPosition(it.animatedValue as Int)
                    }
                    start()
                }
            } else {
                updateBackgroundPosition((-maxWidth * percent).toInt())
            }
        }
    }

    /**
     * 刷新子view的显示
     */
    fun onBind() {
        children.forEach {
            if (it is ScrollFrameLayout) {
                it.onBind()
            }
        }
    }

    /**
     * 返回值为true说明无缓存，使用默认配置
     */
    fun fromSerializationData(): Boolean {
        removeAllTypeWidget("")
        val sp = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
        val widgetListString = sp.getString("widget", "null")
        Log.e("ScrollBackgroundView", "fromWidget: $widgetListString")
        val list = try {
            Gson().fromJson<List<WidgetInf>>(widgetListString, object : TypeToken<List<WidgetInf>>() {}.type)
        } catch (e: Exception) {
            null
        } ?: return true
        post {
            list.forEach {
                addWidget(it.name, it.type, it.posX, it.posY, it.width, it.height)
            }
        }
        return false
    }

    fun saveSerializationData() {
        val sp = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
        val list = mutableListOf<WidgetInf>()
        children.forEach {
            if (it is ScrollFrameLayout && it.scrollChild != null) {
                val positionPair = getChildPosition(it.scrollChild!!.getWidgetType()) ?: return@forEach
                val posX = positionPair.first
                val posY = positionPair.second
                if (!(it.getWidgetType().isNullOrEmpty() || it.getWidgetType() == "fixed"))
                    list.add(
                        WidgetInf(
                            it.getWidgetName(),
                            it.getWidgetType()!!,
                            posX / (mHeight * mSizeFix),
                            posY / mHeight.toFloat(),
                            it.width / (mHeight * mSizeFix),
                            it.height / (mHeight).toFloat()
                        )
                    )
            }
        }
        val editor = sp.edit()
        val widgetListString = Gson().toJson(list)
        editor.putString("widget", widgetListString)
        Log.e("ScrollBackgroundView", "saveWidget: $widgetListString")
        editor.commit()
    }

    fun addWidget(widgetName: String, widgetType: String, posX: Int = 0, posY: Int = 0) {
        post {
            removeWidgetByName(widgetName)
            getWidgetClass(widgetType)?.let {
                addView(it.getChildView(this).also { childView ->
                    childView.mName = widgetName
                    childView.scrollChild = it
                    childView.canMove = it.canMove()
                    val x = (posX / (mBgHeight.toFloat()) * mHeight).toInt()
                    val y = (posY / (mBgHeight.toFloat()) * mHeight).toInt()
                    val w = (it.getWidth(context) / (mBgHeight.toFloat()) * mHeight).toInt()
                    val h = (it.getHeight(context) / (mBgHeight.toFloat()) * mHeight).toInt()
                    childView.layoutParams = LayoutParams(w, h)
                    childView.shouldScrollWithBackground = it.shouldScrollWithBackground()
                    setChildPosition(childView, x, y, w, h, it.shouldScrollWithBackground())
                })
            }
        }
    }

    fun addWidget(widgetName: String, widgetType: String, posXF: Float = 0f, posYF: Float = 0f, widthF: Float, heightF: Float) {
        removeWidgetByName(widgetName)
        getWidgetClass(widgetType)?.let {
            addView(it.getChildView(this).also { childView ->
                childView.mName = widgetName
                childView.scrollChild = it
                childView.canMove = it.canMove()
                childView.layoutParams = LayoutParams((widthF * mHeight * mSizeFix).toInt(), (heightF * mHeight).toInt())
                childView.shouldScrollWithBackground = it.shouldScrollWithBackground()
                val posX = posXF * mHeight * mSizeFix
                val posY = posYF * mHeight

                Log.e("sandyzhang-===", "${posX}, ${posY}, ${(widthF * mHeight * mSizeFix).toInt()}, ${(heightF * mHeight).toInt()}")
                setChildPosition(
                    childView,
                    posX.toInt(),
                    posY.toInt(),
                    (widthF * mHeight * mSizeFix).toInt(),
                    (heightF * mHeight).toInt(),
                    it.shouldScrollWithBackground()
                )
            })
        }
    }

    fun findWidget(name: String): ScrollFrameLayout? {
        children.forEachIndexed { index, view ->
            if (view is ScrollFrameLayout) {
                if (view.getWidgetName() == name) {
                    return view
                }
            }
        }
        return null
    }

    fun removeAllTypeWidget(widgetType: String) {
        var i = 0
        while (i < childCount) {
            val view = getChildAt(i)
            if (view is ScrollFrameLayout) {
                if (view.getWidgetType() == widgetType || widgetType == "") {
                    if (!(view.getWidgetType() == "fixed" || view.getWidgetType() == "Rabbit")) {
                        removeViewAt(i)
                        i--
                    }
                }
            }
            i++
        }
    }

    fun removeWidgetByName(widgetName: String) {
        children.forEachIndexed { index, view ->
            if (view is ScrollFrameLayout) {
                if (view.getWidgetName() == widgetName) {
                    removeViewAt(index)
                }
            }
        }
    }

    fun getVisibleLeft() = -((mBackgroundImage.layoutParams as? LayoutParams?)?.leftMargin ?: 0)

    fun setWallPaper(@DrawableRes id: Int) {
        pasteWidget("wallpaper", 0, 0, MAX_WIDTH, HORIZON + 20, id, 1f, null)
    }

    fun setFloor(@DrawableRes id: Int) {
        pasteWidget("floor", 0, HORIZON, MAX_WIDTH, MAX_HEIGHT, id, 1f, null)
    }

    fun getRealHorizon() = (HORIZON / MAX_HEIGHT.toFloat()) * mBackgroundImage.height
    fun getRealWidth() = mBackgroundImage.width
    fun getRealHeight() = mBackgroundImage.height

    companion object {
        const val MAX_HEIGHT = 1600
        const val MAX_WIDTH = 3000
        const val HORIZON = 900
    }
}