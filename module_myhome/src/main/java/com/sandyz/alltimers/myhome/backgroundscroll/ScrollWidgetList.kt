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
        else -> try {
            Class.forName("com.sandyz.alltimers.myhome.backgroundscroll.$widgetType").newInstance() as ScrollChild
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * 用于保存小组件位置
 */
data class WidgetInf(
    val name: String = "",
    val type: String = "",
    val posX: Float = 0f,
    val posY: Float = 0f,
    val width: Float = 0f,
    val height: Float = 0f
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

    /**
     * 置顶或置底，0为未指定，1为置顶，-1为置底
     */
    abstract fun viewElevation(): Int
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
    override fun viewElevation() = -1
}


class Rabbit : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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
    override fun viewElevation() = 0
}


class Widget1 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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
    override fun viewElevation() = 0
}

class Widget2 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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
    override fun viewElevation() = 0
}


class Widget3 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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
    override fun viewElevation() = 0
}

class Widget4 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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
    override fun viewElevation() = 0
}

class Widget5 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_5

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

    override fun getWidth(context: Context) = 560
    override fun getHeight(context: Context) = 480
    override fun viewElevation() = 0
}

class Widget6 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_6

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
    override fun getHeight(context: Context) = 170
    override fun viewElevation() = 0
}

class Widget7 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_7

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

    override fun getWidth(context: Context) = 150
    override fun getHeight(context: Context) = 270
    override fun viewElevation() = 0
}

class Widget8 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_8

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

    override fun getWidth(context: Context) = 150
    override fun getHeight(context: Context) = 290
    override fun viewElevation() = 0
}

class Widget9 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_9

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

    override fun getWidth(context: Context) = 150
    override fun getHeight(context: Context) = 190
    override fun viewElevation() = 0
}

class Widget10 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_10

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

    override fun getWidth(context: Context) = 260
    override fun getHeight(context: Context) = 220
    override fun viewElevation() = 0
}

class Widget11 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_11

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

    override fun getWidth(context: Context) = 195
    override fun getHeight(context: Context) = 125
    override fun viewElevation() = 0
}

class Widget12 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_12

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

    override fun getWidth(context: Context) = 600
    override fun getHeight(context: Context) = 350
    override fun viewElevation() = -1
}

class Widget13 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_13

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
    override fun getHeight(context: Context) = 180
    override fun viewElevation() = 0
}

class Widget14 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_14

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

    override fun getWidth(context: Context) = 400
    override fun getHeight(context: Context) = 330
    override fun viewElevation() = 0
}

class Widget15 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_15

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

    override fun getWidth(context: Context) = 600
    override fun getHeight(context: Context) = 350
    override fun viewElevation() = -1
}

class Widget16 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_16

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
    override fun getHeight(context: Context) = 440
    override fun viewElevation() = 0
}

class Widget17 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_17

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

    override fun getWidth(context: Context) = 450
    override fun getHeight(context: Context) = 440
    override fun viewElevation() = 0
}

class Widget18 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_18

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

    override fun getWidth(context: Context) = 300
    override fun getHeight(context: Context) = 600
    override fun viewElevation() = 0
}

class Widget19 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_19

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

    override fun getWidth(context: Context) = 210
    override fun getHeight(context: Context) = 600
    override fun viewElevation() = 0
}

class Widget20 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_20

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
    override fun viewElevation() = 0
}

class Widget21 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_21

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

    override fun getWidth(context: Context) = 600
    override fun getHeight(context: Context) = 600
    override fun viewElevation() = 0
}

class Widget22 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_22

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

    override fun getWidth(context: Context) = 310
    override fun getHeight(context: Context) = 600
    override fun viewElevation() = 0
}

class Widget23 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_23

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

    override fun getWidth(context: Context) = 700
    override fun getHeight(context: Context) = 380
    override fun viewElevation() = 0
}

class Widget24 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_24

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

    override fun getWidth(context: Context) = 620
    override fun getHeight(context: Context) = 770
    override fun viewElevation() = 0
}

class Widget25 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_25

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

    override fun getWidth(context: Context) = 410
    override fun getHeight(context: Context) = 320
    override fun viewElevation() = 0
}

class Widget26 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_26

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

    override fun getWidth(context: Context) = 300
    override fun getHeight(context: Context) = 700
    override fun viewElevation() = 0
}

class Widget27 : ScrollChild() {
    private val WIDGET_NAME = this.javaClass.simpleName
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

    var drawableId: Int = R.drawable.myhome_widget_27

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

    override fun getWidth(context: Context) = 630
    override fun getHeight(context: Context) = 580
    override fun viewElevation() = 0
}