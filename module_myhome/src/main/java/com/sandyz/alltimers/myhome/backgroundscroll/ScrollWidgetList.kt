package com.sandyz.alltimers.myhome.backgroundscroll

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.myhome.R


/**
 * 建立一个字符对应小组件类的映射
 */
fun getWidgetClass(widgetType: String): ScrollChild? {
    return when (widgetType) {
        "fixed" -> {
            FixedWidget()
        }
        "widget1" -> {
            Widget1()
        }
        "widget2" -> {
            Widget2()
        }
        "widget3" -> {
            Widget3()
        }
        "widget4" -> {
            Widget4()
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
     * 组件是否受重力影响
     */
    abstract fun withGravity(): Boolean

    /**
     * 大背景开始移动通知，应停止交互
     */
    abstract fun startMove(v: View)

    /**
     * 大背景停止移动通知
     */
    abstract fun finishMove(v: View)
}


class FixedWidget : ScrollChild() {
    override fun getWidgetType() = "fixed"
    override fun shouldScrollWithBackground() = true
    override fun canMove() = false
    override fun withGravity() = false

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
        v.elevation = 0f
    }

    override fun getWidth(context: Context) = context.dp2px(150)
    override fun getHeight(context: Context) = context.dp2px(150)
}


class Widget1 : ScrollChild() {
    companion object{
        const val WIDGET_NAME = "widget1"
    }
    override fun getWidgetType() = WIDGET_NAME
    override fun shouldScrollWithBackground() = true
    override fun canMove() = true
    override fun withGravity() = true

    override fun startMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.stop()
    }

    override fun finishMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.start()
    }

    var drawableId: Int = R.drawable.myhome_widget_1

    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = WIDGET_NAME
        iv.scaleType = ImageView.ScaleType.FIT_XY
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>(WIDGET_NAME)?.let {
            if (drawableId != 0) {
                Glide.with(v.context).load(drawableId).into(it)
            }
        }
    }

    override fun getWidth(context: Context) = 900
    override fun getHeight(context: Context) = 500
}

class Widget2 : ScrollChild() {
    companion object{
        const val WIDGET_NAME = "widget2"
    }
    override fun getWidgetType() = WIDGET_NAME
    override fun shouldScrollWithBackground() = true
    override fun canMove() = true
    override fun withGravity() = true

    override fun startMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.stop()
    }

    override fun finishMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.start()
    }

    var drawableId: Int = R.drawable.myhome_widget_2

    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = WIDGET_NAME
        iv.scaleType = ImageView.ScaleType.FIT_XY
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>(WIDGET_NAME)?.let {
            if (drawableId != 0) {
                Glide.with(v.context).load(drawableId).into(it)
            }
        }
    }

    override fun getWidth(context: Context) = 200
    override fun getHeight(context: Context) = 500
}


class Widget3 : ScrollChild() {
    companion object{
        const val WIDGET_NAME = "widget3"
    }
    override fun getWidgetType() = WIDGET_NAME
    override fun shouldScrollWithBackground() = true
    override fun canMove() = true
    override fun withGravity() = false

    override fun startMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.stop()
    }

    override fun finishMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.start()
    }

    var drawableId: Int = R.drawable.myhome_widget_3

    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = WIDGET_NAME
        iv.scaleType = ImageView.ScaleType.FIT_XY
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>(WIDGET_NAME)?.let {
            if (drawableId != 0) {
                Glide.with(v.context).load(drawableId).into(it)
            }
        }
    }

    override fun getWidth(context: Context) = 550
    override fun getHeight(context: Context) = 100
}

class Widget4 : ScrollChild() {
    companion object{
        const val WIDGET_NAME = "widget4"
    }
    override fun getWidgetType() = WIDGET_NAME
    override fun shouldScrollWithBackground() = true
    override fun canMove() = true
    override fun withGravity() = true

    override fun startMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.stop()
    }

    override fun finishMove(v: View) {
        (v.findViewWithTag<ImageView>(WIDGET_NAME)?.drawable as? GifDrawable?)?.start()
    }

    var drawableId: Int = R.drawable.myhome_widget_4

    override fun getChildView(parent: View): ScrollFrameLayout {
        val scrollFrameLayout = ScrollFrameLayout(parent.context)
        val iv = ImageView(parent.context)
        iv.tag = WIDGET_NAME
        iv.scaleType = ImageView.ScaleType.FIT_XY
        scrollFrameLayout.addView(iv)
        return scrollFrameLayout
    }

    override fun onBind(v: View) {
        v.findViewWithTag<ImageView>(WIDGET_NAME)?.let {
            if (drawableId != 0) {
                Glide.with(v.context).load(drawableId).into(it)
            }
        }
    }

    override fun getWidth(context: Context) = 350
    override fun getHeight(context: Context) = 400
}