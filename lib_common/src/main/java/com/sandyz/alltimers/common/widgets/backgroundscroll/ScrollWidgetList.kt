package com.sandyz.alltimers.common.widgets.backgroundscroll

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sandyz.alltimers.common.R
import com.sandyz.alltimers.common.extensions.dp2px
import java.lang.Exception

fun getWidgetClass(widgetName: String): ScrollChild? {
    return when (widgetName) {
        "recorder" -> {
            RecorderWidget()
        }
        else -> null
    }
}

data class WidgetInf(
    val name: String = "",
    val posX: Int = 0,
    val posY: Int = 0,
    val width: Int = 0,
    val height: Int = 0
)

fun ScrollBackgroundView.fromSerializationData() {
    val sp = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
    val widgetListString = sp.getString("widget", "[]")
    Log.e("ScrollBackgroundView", "fromWidget: $widgetListString")
    val list = try {
        Gson().fromJson<List<WidgetInf>>(widgetListString, object : TypeToken<List<WidgetInf>>() {}.type)
    }catch (e: Exception){
        mutableListOf()
    }
    list.forEach {
        addWidget(it.name, it.posX, it.posY)
    }
}

fun ScrollBackgroundView.saveSerializationData() {
    val sp = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
    val list = mutableListOf<WidgetInf>()
    children.forEach {
        if (it is ScrollFrameLayout && it.scrollChild != null) {
            val positionPair = getChildPosition(it.scrollChild!!.getName())?: return@forEach
            val posX = positionPair.first
            val posY = positionPair.second
            if (!it.getName().isNullOrEmpty())
                list.add(WidgetInf(it.getName()!!, posX, posY, it.scrollChild!!.getWidthDp(), it.scrollChild!!.getHeightDp()))
        }
    }
    val editor = sp.edit()
    val widgetListString = Gson().toJson(list)
    editor.putString("widget", widgetListString)
    Log.e("ScrollBackgroundView", "saveWidget: $widgetListString")
    editor.apply()
}

fun ScrollBackgroundView.addWidget(widgetName: String, posX: Int = 0, posY: Int = 0) {
    removeWidget(widgetName)
    getWidgetClass(widgetName)?.let {
        addView(it.getChildView(this).also { childView ->
            childView.scrollChild = it
            childView.layoutParams = FrameLayout.LayoutParams(context.dp2px(it.getWidthDp()), context.dp2px(it.getHeightDp()))
            setChildPosition(childView, posX, posY)
        })
    }
}

fun ScrollBackgroundView.removeWidget(widgetName: String) {
    children.forEachIndexed { index, view ->
        if (view is ScrollFrameLayout) {
            if (view.getName() == widgetName) {
                removeViewAt(index)
                return
            }
        }
    }
}

abstract class ScrollChild {
    abstract fun getName(): String
    abstract fun getChildView(parent: View): ScrollFrameLayout
    abstract fun onBind(v: View)
    abstract fun getWidthDp(): Int
    abstract fun getHeightDp(): Int
}

class RecorderWidget : ScrollChild() {
    override fun getName() = "recorder"
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

    override fun getWidthDp() = 150

    override fun getHeightDp() = 150
}