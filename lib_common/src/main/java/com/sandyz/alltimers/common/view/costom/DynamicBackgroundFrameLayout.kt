package com.sandyz.alltimers.common.view.costom


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.utils.BitmapLoader

class DynamicBackgroundFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var bgSrcId = 0
    private var bgBitmap: Bitmap? = null
    private var mWidth = 0
    private var mHeight = 0
    private val mHandler by lazy { handler }


    private var offset = 0
        set(value) {
            field = value % mWidth
        }
    private val paint = Paint()

    init {
        setWillNotDraw(false)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicBackgroundFrameLayout)

        bgSrcId = typedArray.getResourceId(R.styleable.DynamicBackgroundFrameLayout_background_src, 0)

        typedArray.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        bgBitmap = BitmapLoader.decodeBitmapFromResourceByWidth(resources, bgSrcId, mWidth / 6)
        mHandler.removeCallbacks(animationRunnable)
        mHandler?.post(animationRunnable)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (y in -mWidth..mHeight step mWidth / 2) {
            for (x in -mWidth..mWidth step mWidth / 2) {
                drawBackGroundPic(canvas, x.toFloat(), y.toFloat())
                drawBackGroundPic(canvas, x.toFloat() + mWidth / 4, y.toFloat() + mWidth / 4)

            }
        }
    }

    private fun drawBackGroundPic(canvas: Canvas?, x: Float, y: Float) {
        val bitmap = bgBitmap ?: return
        val drawX = x + offset
        val drawY = y + offset
        if (drawX > -bitmap.width && drawX < bitmap.width + mWidth && drawY > -bitmap.height && drawY < mHeight + bitmap.height) {
            canvas?.drawBitmap(bitmap, drawX, drawY, paint)
        }
    }

    private val animationRunnable = object : Runnable {
        override fun run() {
            offset += 2
            mHandler?.postDelayed(this, 20)
            invalidate()
        }
    }

}