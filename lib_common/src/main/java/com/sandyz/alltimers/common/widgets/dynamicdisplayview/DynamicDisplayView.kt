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
import androidx.core.view.ViewCompat
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.sp
import com.sandyz.alltimers.common.utils.BitmapLoader
import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.common.widgets.dynamicdisplayview.drawer.OvershootDrawerImpl

class DynamicDisplayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mWidth = 0
    private var mHeight = 0
    var mTextSize = 24
    private val bitmapMap = mutableMapOf<Char, Bitmap?>()
    private val onAnimRunnable = object : Runnable {
        override fun run() {
            invalidate()
        }
    }
    private val mAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            if (it.animatedValue as Float >= 1.0) {
                it.cancel()
            }
            ViewCompat.postOnAnimation(this@DynamicDisplayView, onAnimRunnable)
        }

    }
    var drawer: Drawer? = null

    interface Drawer {
        fun drawBitmap(view: DynamicDisplayView, canvas: Canvas?, fraction: Float, originalChar: Char, targetChar: Char, h: Int, w: Int)
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
            mAnimator.interpolator = drawer?.getInterpolator()
            mAnimator.duration = drawer?.getDuration()?: 300L
            mAnimator.start()
        }

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicDisplayView)
        mText = typedArray.getString(R.styleable.DynamicDisplayView_text) ?: ""
        mTextSize = typedArray.getInt(R.styleable.DynamicDisplayView_textSize, 24)
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
        ResourceGetter.getDrawableId(R.drawable::class.java,"common_ic_background_test")
    } else {
        0
    }


    fun getBitmap(c: Char): Bitmap? {
        // 如果缓存中有则直接取。
        return if (bitmapMap.containsKey(c)) {
            bitmapMap[c]
        } else {
            BitmapLoader.decodeBitmapFromResource(
                resources,
                getDrawableId(c),
                (mHeight * CHAR_WIDTH_PROPORTION).toInt(),
                mHeight
            ).also {
                bitmapMap[c] = it
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 当文本长度相同时才更新动画
        if (mText.length == preText.length) {
            mText.forEachIndexed { index, char ->
                if (char != preText[index] && mAnimator.isStarted) {
//                    val originBitmap = getBitmap(preText[index])
//                    val targetBitmap = getBitmap(mText[index])
                    canvas?.save()
                    drawer?.drawBitmap(
                        this,
                        canvas?.apply { translate(index * mHeight * CHAR_WIDTH_PROPORTION, 0f) },
                        mAnimator.animatedValue as Float,
                        preText[index], mText[index],
                        mHeight,
                        (mHeight * CHAR_WIDTH_PROPORTION).toInt()
                    )
                    canvas?.restore()
                } else {
                    canvas?.save()
                    drawer?.drawBitmap(
                        this,
                        canvas?.apply { translate(index * mHeight * CHAR_WIDTH_PROPORTION, 0f) },
                        1f,
                        ' ',
                        char,
                        mHeight,
                        (mHeight * CHAR_WIDTH_PROPORTION).toInt()
                    )
                    canvas?.restore()
                }
            }
        } else {
            mText.forEachIndexed { index, char ->
                canvas?.save()
                drawer?.drawBitmap(
                    this,
                    canvas?.apply { translate(index * mHeight * CHAR_WIDTH_PROPORTION, 0f) },
                    1f,
                    ' ',
                    char,
                    mHeight,
                    (mHeight * CHAR_WIDTH_PROPORTION).toInt()
                )
                canvas?.restore()
            }
        }

    }

    companion object {
        private const val CHAR_WIDTH_PROPORTION = 0.6f
    }

}