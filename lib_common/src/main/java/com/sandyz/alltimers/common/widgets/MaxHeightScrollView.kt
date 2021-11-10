package com.sandyz.alltimers.common.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ScrollView
import com.sandyz.alltimers.common.R


class MaxHeightScrollView : ScrollView {
    private var mMaxHeight = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView)
        mMaxHeight = typedArray.getLayoutDimension(R.styleable.MaxHeightScrollView_maxHeight, mMaxHeight)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecT = heightMeasureSpec
        if (mMaxHeight > 0) {
            heightMeasureSpecT = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecT)
    }
}