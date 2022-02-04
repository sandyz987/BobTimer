package com.sandyz.alltimers.common.widgets.dynamicdisplayview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.utils.BitmapLoader
import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer.OvershootDrawerImpl

class DynamicDisplayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mWidth = 0
    private var mHeight = 0
    private val bitmapMap = mutableMapOf<Char, Bitmap>()
    private val mAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            if (it.animatedValue as Float >= 1.0) {
                it.cancel()
            }
            invalidate()
        }
    }
    var drawer: Drawer = OvershootDrawerImpl()

    interface Drawer {
        fun drawBitmap(canvas: Canvas?, fraction: Float, originBitmap: Bitmap?, targetBitmap: Bitmap?, h: Int)
        fun getInterpolator(): Interpolator
        fun getDuration(): Long
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 允许绘制超出边界
        (parent as ViewGroup).clipChildren = false
    }

    private var preText = ""
    var mText = ""
        set(value) {
            preText = field
            field = value
            requestLayout()
            if (mAnimator.isStarted) {
                mAnimator.cancel()
            }
            mAnimator.interpolator = drawer.getInterpolator()
            mAnimator.duration = drawer.getDuration()
            mAnimator.start()
        }

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicTimeDisplayView)
        mText = typedArray.getString(R.styleable.DynamicTimeDisplayView_text) ?: ""
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = mText.length * MeasureSpec.getSize(heightMeasureSpec) * CHAR_WIDTH_PROPORTION
        val widthMS = MeasureSpec.makeMeasureSpec(width.toInt(), MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMS, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        bitmapMap.clear()
    }

    /**
     * 要显示的各个字符的图片的id
     */
    private fun getDrawableId(c: Char): Int = if (c in '0'..'9') {
        ResourceGetter.getDrawableId("common_ic_background_test")
    } else {
        0
    }


    private fun getBitmap(c: Char): Bitmap? {
        // 如果缓存中有则直接取。
        return bitmapMap[c] ?: BitmapLoader.decodeBitmapFromResource(
            resources,
            getDrawableId(c),
            (mHeight * CHAR_WIDTH_PROPORTION).toInt(),
            mHeight
        )?.also {
            bitmapMap[c] = it
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 当文本长度相同时才更新动画
        if (mText.length == preText.length) {
            mText.forEachIndexed { index, char ->
                if (char != preText[index] && mAnimator.isStarted) {
                    val originBitmap = getBitmap(preText[index])
                    val targetBitmap = getBitmap(mText[index])
                    canvas?.save()
                    drawer.drawBitmap(
                        canvas?.apply {
                            translate(index * mHeight * CHAR_WIDTH_PROPORTION, 0f)
                        },
                        mAnimator.animatedValue as Float,
                        originBitmap, targetBitmap, mHeight
                    )
                    canvas?.restore()
                } else {
                    val bitmap = getBitmap(char)
                    canvas?.save()
                    drawer.drawBitmap(
                        canvas?.apply {
                            translate(index * mHeight * CHAR_WIDTH_PROPORTION, 0f)
                        },
                        1f,
                        null,
                        bitmap,
                        mHeight
                    )
                    canvas?.restore()
                }
            }
        } else {
            mText.forEachIndexed { index, char ->
                val bitmap = getBitmap(char)
                canvas?.save()
                drawer.drawBitmap(
                    canvas?.apply {
                        translate(index * mHeight * CHAR_WIDTH_PROPORTION, 0f)
                    },
                    1f,
                    null,
                    bitmap,
                    mHeight
                )
                canvas?.restore()
            }
        }

    }

    companion object {
        private const val CHAR_WIDTH_PROPORTION = 0.7f
    }

}