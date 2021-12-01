package com.sandyz.alltimers.common.widgets.backgroundscroll

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.dp2px


/**
 * 建立一个字符对应小组件类的映射
 */
fun getWidgetClass(widgetName: String): ScrollChild? {
    return when (widgetName) {
        "recorder" -> {
            RecorderWidget()
        }
        "rabbit" -> {
            RabbitWidget()
        }
        else -> null
    }
}

/**
 * 用于保存小组件位置
 */
data class WidgetInf(
    val name: String = "",
    val posX: Int = 0,
    val posY: Int = 0,
    val width: Int = 0,
    val height: Int = 0
)

/**
 * 抽象出来每一个小组件都应该有这些方法
 */
abstract class ScrollChild {
    /**
     * 组件名称
     */
    abstract fun getName(): String

    /**
     * 组件布局
     */
    abstract fun getChildView(parent: View): ScrollFrameLayout

    /**
     * 组件刷新显示
     */
    abstract fun onBind(v: View)

    /**
     * 组件宽度
     */
    abstract fun getWidth(context: Context): Int

    /**
     * 组件高度
     */
    abstract fun getHeight(context: Context): Int

    /**
     * 组件是否应该跟随背景拖动
     */
    abstract fun shouldScrollWithBackground(): Boolean
}

class RecorderWidget : ScrollChild() {
    override fun getName() = "recorder"
    override fun shouldScrollWithBackground() = true
    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = "RecorderImageView"
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>("RecorderImageView")?.let {
            Glide.with(v.context).load(R.drawable.common_ic_gif).into(it)
        }
    }

    override fun getWidth(context: Context) = context.dp2px(150)
    override fun getHeight(context: Context) = context.dp2px(150)
}

class RabbitWidget : ScrollChild() {
    override fun getName() = "rabbit"
    override fun shouldScrollWithBackground() = false
    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = "rabbit"
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>("rabbit")?.let {
            Glide.with(v.context).load(R.drawable.common_ic_rabbit).into(it)
        }
    }

    override fun getWidth(context: Context) = context.dp2px(150)
    override fun getHeight(context: Context) = context.dp2px(150)
}