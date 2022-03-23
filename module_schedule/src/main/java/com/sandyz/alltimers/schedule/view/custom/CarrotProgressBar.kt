package com.sandyz.alltimers.schedule.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.schedule.R

class CarrotProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0
    private var carrotDrawable = ResourcesCompat.getDrawable(resources, R.drawable.schedule_ic_carrot, null)
    private var pathBg: Path? = null
    private var pathCover: Path? = null
    private val mHandler by lazy { handler }
    private var pathStripe: Path = Path()
    private val rx = floatArrayOf(
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f,
        context.dp2px(PROGRESS_WIDTH_DP) / 2f
    )
    private var offset = 0
        set(value) {
            field = value % (2 * context.dp2px(PROGRESS_STRIPE_WIDTH_DP))
        }
    private var paint = Paint().apply {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
    }
    var progress: Float = 0f
        set(value) {
            field = value
            refresh()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        refresh()
        mHandler.removeCallbacks(animationRunnable)
        mHandler?.post(animationRunnable)
    }

    private fun refresh() {
        carrotDrawable?.setBounds(0, 0, mHeight, mHeight)
        val padding = context.dp2px(PADDING_DP).toFloat()
        val middleY = mHeight / 2f
        val progressLength = (mWidth - 2 * padding) * progress
        val progressWidth = context.dp2px(PROGRESS_WIDTH_DP)
        pathBg = Path().apply {
            addRoundRect(padding, middleY - progressWidth / 2f, mWidth - padding, middleY + progressWidth / 2f, rx, Path.Direction.CCW)
        }
        pathCover = Path().apply {
            addRoundRect(padding, middleY - progressWidth / 2f, padding + progressLength, middleY + progressWidth / 2f, rx, Path.Direction.CCW)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 画进度和背景
        paint.color = resources.getColor(R.color.schedule_color_progress_background)
        pathBg?.let { canvas?.drawPath(it, paint) }
        paint.color = resources.getColor(R.color.schedule_color_progress_cover)
        pathCover?.let { canvas?.drawPath(it, paint) }


        // 画条纹
        paint.color = resources.getColor(R.color.schedule_color_progress_stripe)
        pathStripe.reset()
        val stripeWidth = context.dp2px(PROGRESS_STRIPE_WIDTH_DP)
        pathStripe.apply {
            for (i in -5 * stripeWidth..mWidth step stripeWidth * 2) {
                moveTo((i + offset).toFloat(), -mHeight.toFloat())
                lineTo((i + offset + stripeWidth).toFloat(), -mHeight.toFloat())
                lineTo((i + offset + stripeWidth).toFloat() + 2 * mHeight, -mHeight.toFloat() + 2 * mHeight)
                lineTo((i + offset).toFloat() + 2 * mHeight, -mHeight.toFloat() + 2 * mHeight)
                close()
            }
        }
        pathCover?.let { pathStripe.op(it, Path.Op.INTERSECT) }
        canvas?.drawPath(pathStripe, paint)


        // 画萝卜
        val padding = context.dp2px(PADDING_DP).toFloat()
        val progressWidth = (mWidth - 2 * padding) * progress
        canvas?.save()
        canvas?.apply {
            translate(padding + progressWidth - mHeight / 2f, -context.dp2px(2).toFloat())
            carrotDrawable?.draw(this)
        }
        canvas?.restore()

    }

    private val animationRunnable = object : Runnable {
        override fun run() {
            offset += 2
            mHandler?.postDelayed(this, 20)
            invalidate()
        }
    }

    companion object {
        private const val PADDING_DP = 8
        private const val PROGRESS_WIDTH_DP = 10
        private const val PROGRESS_STRIPE_WIDTH_DP = 16
    }
}