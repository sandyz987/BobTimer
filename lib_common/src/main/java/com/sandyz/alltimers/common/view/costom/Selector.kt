package com.sandyz.alltimers.common.view.costom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.sandyz.alltimers.common.BaseApp
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.extensions.drawTextCenter
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.sp

class Selector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0

    var isSingleSelect = false

    private var colorId: Int = Color.BLACK
    private var textSizeSp: Float = context.sp(16).toFloat()

    private var selectDrawableId = 0
    private var unselectDrawableId = 0
    private var iconDrawableId = 0

    private var text: String? = ""

    private var iconSize: Int = context.dp2px(24)

    var isSelect = false
        set(value) {
            field = value
            invalidate()
        }


    private val paint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = colorId
            textSize = textSizeSp
            typeface = ResourcesCompat.getFont(BaseApp.context, R.font.summer_typeface)
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Selector)

        textSizeSp = context.sp(typedArray.getInt(R.styleable.Selector_hintSize, 16)).toFloat()
        text = typedArray.getString(R.styleable.Selector_hint)
        selectDrawableId = typedArray.getResourceId(R.styleable.Selector_selectDrawable, 0)
        unselectDrawableId = typedArray.getResourceId(R.styleable.Selector_unselectDrawable, 0)
        iconDrawableId = typedArray.getResourceId(R.styleable.Selector_iconDrawable, 0)
        iconSize = context.dp2px(typedArray.getInt(R.styleable.Selector_icSize, 16))
        isSingleSelect = typedArray.getBoolean(R.styleable.Selector_isSingleSelect, false)
        isSelect = typedArray.getBoolean(R.styleable.Selector_isSelect, false)
        colorId = typedArray.getColor(R.styleable.Selector_hintColor, Color.BLACK)


        typedArray.recycle()

        setOnClickAction {
            isSelect = !isSelect
            if (isSingleSelect) {
                if (isSelect) {
                    (parent as? LinearLayout?)?.children?.forEach {
                        if (it is Selector && it != this && it.isSingleSelect) {
                            it.isSelect = false
                        }
                    }
                } else {
                    var hasOtherSelected = false
                    (parent as? LinearLayout?)?.children?.forEach {
                        if (it is Selector && it != this && it.isSingleSelect) {
                            if (it.isSelect) {
                                hasOtherSelected = true
                            }
                        }
                    }
                    if (!hasOtherSelected) {
                        isSelect = true
                    }
                }
            }
        }
    }

    private val selectDrawable by lazy {
        if (selectDrawableId != 0) {
            ResourcesCompat.getDrawable(resources, selectDrawableId, null)?.apply {
                setBounds(0, 0, iconSize, iconSize)
            }
        } else null
    }
    private val unselectDrawable by lazy {
        if (unselectDrawableId != 0) {
            ResourcesCompat.getDrawable(resources, unselectDrawableId, null)?.apply {
                setBounds(0, 0, iconSize, iconSize)
            }
        } else null
    }
    private val iconDrawable by lazy {
        if (iconDrawableId != 0) {
            ResourcesCompat.getDrawable(resources, iconDrawableId, null)?.apply {
                setBounds(0, 0, iconSize, iconSize)
            }
        } else null
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val padding = context.dp2px(PADDING_DP).toFloat()

        if (iconDrawable != null) {
            canvas?.save()
            canvas?.translate(padding, (mHeight - iconSize) / 2f)
            canvas?.let { iconDrawable?.draw(it) }
            canvas?.restore()
        }

        canvas?.save()
        text?.let {
            canvas?.drawTextCenter(
                it,
                padding + if (iconDrawable != null) iconSize + context.dp2px(8) else 0,
                mHeight / 2f,
                paint,
                Paint.Align.LEFT
            )
        }
        canvas?.translate(mWidth - padding - iconSize, (mHeight - iconSize) / 2f)
        if (isSelect) {
            canvas?.let { selectDrawable?.draw(it) }
        } else {
            canvas?.let { unselectDrawable?.draw(it) }
        }
        canvas?.restore()
    }

    companion object {
        const val PADDING_DP = 16
    }
}