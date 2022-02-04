package com.sandyz.alltimers.main.view.costom


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.extensions.drawTextBottom
import com.sandyz.alltimers.common.extensions.sp
import com.sandyz.alltimers.common.utils.BitmapLoader
import com.sandyz.alltimers.main.R

class BottomNavButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint()
    private lateinit var anim: ValueAnimator
    private var textWidth = 0f
    private var textHeight = 0f
    private val margin = context.dp2px(2)
    private var text: String = ""
        set(value) {
            field = value
            paint.textSize = context.sp(14).toFloat()
            val metrics = Paint.FontMetrics()
            paint.getFontMetrics(metrics)
            textWidth = paint.measureText(text)
            textHeight = if (text.isNotBlank()) metrics.bottom - metrics.top else 0f
            selectedBitmap = BitmapLoader.decodeBitmapFromResource(resources, selectedPicId, mWidth, (mHeight - textHeight - margin).toInt())
            noSelectedBitmap = BitmapLoader.decodeBitmapFromResource(resources, noSelectedPicId, mWidth, (mHeight - textHeight - margin).toInt())
        }

    private var noSelectedPicId = 0
    private var selectedBitmap: Bitmap? = null
    private var noSelectedBitmap: Bitmap? = null
    private var selectedPicId = 0
    private var textColor = 0
    private var trX = 0f
    var mSelected = false
        set(value) {
            field = value
            invalidate()
        }
    private var mWidth = 0
    private var mHeight = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavButton)

        paint.isAntiAlias = true
        noSelectedPicId = typedArray.getResourceId(R.styleable.BottomNavButton_no_checked_src, 0)
        selectedPicId = typedArray.getResourceId(R.styleable.BottomNavButton_checked_src, 0)
        textColor = typedArray.getColor(R.styleable.BottomNavButton_label_color, 0)
        paint.color = textColor
        text = typedArray.getString(R.styleable.BottomNavButton_label) ?: ""

        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h
        mWidth = w
        text = text
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mSelected) {
            selectedBitmap?.let { canvas?.drawBitmap(it, trX, 0f, paint) }
        } else {
            noSelectedBitmap?.let { canvas?.drawBitmap(it, trX, 0f, paint) }
        }
        canvas?.drawTextBottom(text, mWidth / 2f + trX, mHeight.toFloat(), paint, Paint.Align.CENTER)

    }


    override fun performClick(): Boolean {
        startAnimation1()
        return super.performClick()
    }


    private fun startAnimation1() {
        anim = ValueAnimator.ofFloat(0f, 10f, 0f, -10f, 0f, 10f, 0f)
        anim.repeatCount = 0
        anim.repeatMode = ValueAnimator.REVERSE
        anim.duration = 300
        anim.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            trX = value
            postInvalidate()
        }
        anim.start()
    }
}