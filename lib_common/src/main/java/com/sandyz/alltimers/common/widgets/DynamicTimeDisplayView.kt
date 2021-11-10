package com.sandyz.alltimers.common.widgets

import android.animation.FloatEvaluator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.utils.LoadBitmapUtils
import com.sandyz.alltimers.common.utils.ResourceGetter

class DynamicTimeDisplayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mWidth = 0
    private var mHeight = 0
    private val drawablePaint = Paint()
    private val bitmapMap = mutableMapOf<Char, Bitmap>()
    private val mAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            if (it.animatedValue as Float >= 1.0) {
                it.cancel()
            }
            invalidate()
        }
    }
    var drawer: Drawer = LinearDrawerImpl()
    var duration: Long = 400L
    var interpolator = LinearInterpolator()

    interface Drawer {
        fun drawBitmap(canvas: Canvas?, fraction: Float, originBitmap: Bitmap?, targetBitmap: Bitmap?)
    }

    inner class LinearDrawerImpl : Drawer {
        override fun drawBitmap(canvas: Canvas?, fraction: Float, originBitmap: Bitmap?, targetBitmap: Bitmap?) {
            originBitmap?.let {
                canvas?.save()
                canvas?.translate(0f, -mHeight * fraction)
                canvas?.drawBitmap(it, 0f, 0f, drawablePaint)
                canvas?.restore()
            }
            targetBitmap?.let {
                canvas?.save()
                canvas?.translate(0f, -mHeight * fraction + mHeight)
                canvas?.drawBitmap(it, 0f, 0f, drawablePaint)
                canvas?.restore()
            }
        }
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
            mAnimator.interpolator = interpolator
            mAnimator.duration = duration
            mAnimator.start()
        }

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicTimeDisplayView)
        mText = typedArray.getString(R.styleable.DynamicTimeDisplayView_text) ?: ""
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = mText.length * MeasureSpec.getSize(heightMeasureSpec) * CHAR_WIDTH_PROPORTION
        Log.e("Sandyzhang", "onMeasure: ${mText}, ${width}")
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
        return bitmapMap[c] ?: LoadBitmapUtils.decodeBitmapFromResource(
            resources,
            getDrawableId(c),
            mHeight * CHAR_WIDTH_PROPORTION,
            mHeight
        )?.also {
            Log.e("sandyzhang", "pic: ${it.width}, ${it.height}")
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
                        originBitmap, targetBitmap
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
                        bitmap
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
                    bitmap
                )
                canvas?.restore()
            }
        }

    }

    companion object {
        private const val CHAR_WIDTH_PROPORTION = 0.7f
    }

}