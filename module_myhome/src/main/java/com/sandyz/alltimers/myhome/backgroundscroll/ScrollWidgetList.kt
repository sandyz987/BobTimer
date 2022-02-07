package com.sandyz.alltimers.myhome.backgroundscroll

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.sandyz.alltimers.common.extensions.dp2px


/**
 * 建立一个字符对应小组件类的映射
 */
fun getWidgetClass(widgetType: String): ScrollChild? {
    return when (widgetType) {
        "fixed" -> {
            FixedWidget()
        }
        else -> null
    }
}

/**
 * 用于保存小组件位置
 */
data class WidgetInf(
    val name: String = "",
    val type: String = "",
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
     * 组件种类
     */
    abstract fun getWidgetType(): String

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

    /**
     * 组件是否可以移动
     */
    abstract fun canMove(): Boolean

    /**
     * 大背景开始移动
     */
    abstract fun startMove(v: View)

    /**
     * 大背景停止移动
     */
    abstract fun finishMove(v: View)
}


class FixedWidget : ScrollChild() {
    override fun getWidgetType() = "fixedWidget"
    override fun shouldScrollWithBackground() = true
    override fun canMove() = false
    override fun startMove(v: View) {
        (v.findViewWithTag<ImageView>("fixed")?.drawable as? GifDrawable?)?.stop()
    }

    override fun finishMove(v: View) {
        (v.findViewWithTag<ImageView>("fixed")?.drawable as? GifDrawable?)?.start()
    }

    var drawableId: Int = 0

    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = "fixed"
        iv.scaleType = ImageView.ScaleType.FIT_XY
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>("fixed")?.let {
            if (drawableId != 0) {
                Glide.with(v.context).load(drawableId).into(it)
            }
        }
    }

    override fun getWidth(context: Context) = context.dp2px(150)
    override fun getHeight(context: Context) = context.dp2px(150)
}