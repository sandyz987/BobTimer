package com.sandyz.alltimers.main.view.costom


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.main.R
import kotlin.math.pow
import kotlin.math.sinh
import kotlin.math.sqrt


class BottomNavConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0

    private var strokePath: Path? = null
    private var fillPath: Path? = null

    var includeRate = 0.3f
        set(value) {
            field = value
            refresh()
        }

    private var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = context.dp2px(2).toFloat()
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    init {
        setWillNotDraw(false)
        refresh()
    }


    private fun refresh() {
        post {
            val middleView = findViewById<View>(R.id.main_bottom_navi_add)
            val lineY = middleView.height * (1 - includeRate) + middleView.top
            strokePath = Path().apply {
                val r = middleView.height / 2f
                val margin = dp(20)
                val radius = dp(15)
                val arcR = (margin - radius).toFloat()
                val angleSin = ((includeRate - 0.5) * r * 2 - radius) / (r + margin)
                val angleCos = sqrt(1 - (angleSin).pow(2.0))
                var radiusCircleX = (mWidth / 2f - angleCos * (r + margin)).toFloat()
                val radiusCircleY = lineY + radius
                val radiusSweepDegree = 90 + (sinh(angleSin) * 180 / Math.PI)

                moveTo(0f, lineY)
                lineTo(radiusCircleX, lineY)
                // 画小弧线
                arcTo(
                    radiusCircleX - radius,
                    lineY,
                    radiusCircleX + radius,
                    radiusCircleY + radius,
                    -90f,
                    radiusSweepDegree.toFloat(),
                    false
                )
                // 画大弧线
                arcTo(
                    middleView.left - arcR,
                    middleView.top - arcR,
                    middleView.right + arcR,
                    middleView.bottom + arcR,
                    (90 + radiusSweepDegree).toFloat(),
                    -(2 * radiusSweepDegree).toFloat(),
                    false
                )
                radiusCircleX = (mWidth / 2f + angleCos * (r + margin)).toFloat()

                // 画小弧线
                arcTo(
                    radiusCircleX - radius,
                    lineY,
                    radiusCircleX + radius,
                    radiusCircleY + radius,
                    -90f - radiusSweepDegree.toFloat(),
                    radiusSweepDegree.toFloat(),
                    false
                )

                lineTo(radiusCircleX, lineY)
                lineTo(mWidth.toFloat(), lineY)
            }
            strokePath?.let {
                fillPath = Path().apply {
                    addPath(it)
                    lineTo(mWidth.toFloat(), mHeight.toFloat())
                    lineTo(0f, mHeight.toFloat())
                    lineTo(0f, lineY)
                }
            }

            invalidate()
        }
    }

    private fun dp(dpValue: Int) = context.dp2px(dpValue)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h
        mWidth = w

    }


    override fun draw(canvas: Canvas?) {
        paint.color = Color.parseColor("#FCC5BE")
        paint.style = Paint.Style.FILL
        fillPath?.let { canvas?.drawPath(it, paint) }

        paint.color = Color.parseColor("#B58165")
        paint.style = Paint.Style.STROKE
        strokePath?.let { canvas?.drawPath(it, paint) }

        super.draw(canvas)
    }

}