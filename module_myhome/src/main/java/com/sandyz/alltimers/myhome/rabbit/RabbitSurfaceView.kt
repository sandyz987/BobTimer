package com.sandyz.alltimers.myhome.rabbit

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.myhome.R

/**
 *@author zhangzhe
 *@date 2022/3/29
 *@description
 */

class RabbitSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var sp: SharedPreferences = context.getSharedPreferences("wear", Context.MODE_PRIVATE)

    val type: String

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RabbitSurfaceView)

        type = typedArray.getString(R.styleable.RabbitSurfaceView_view_type) ?: ""

        typedArray.recycle()
    }

    fun removeSurfaceImage(idName: String) {
        var i = 0
        while (i < childCount) {
            val v = getChildAt(i)
            if (v.tag == idName) {
                removeView(v)
                i--
            }
            i++
        }
    }

    fun addSurfaceImage(idName: String) {
        removeSurfaceImage(idName)
        val id = ResourceGetter.getDrawableId(R.drawable::class.java, idName)
        val iv = ImageView(context).apply {
            tag = idName
            scaleType = ImageView.ScaleType.FIT_XY
        }
        Glide.with(context).load(id).into(iv)
        addView(iv)
    }

    fun hasSurface(idName: String): Boolean {
        var flag = false
        children.forEach {
            if (it.tag == idName) flag = true
        }
        return flag
    }

    fun fromSerializationData() {
        removeAllViews()
        val listString = sp.getString(type, "null")
        Log.e("RabbitSurfaceView", "fromSer:$type $listString")
        val list = try {
            Gson().fromJson<List<String>>(listString, object : TypeToken<List<String>>() {}.type)
        } catch (e: Exception) {
            null
        } ?: return
        list.forEach {
            addSurfaceImage(it)
        }
    }

    fun saveSerializationData() {
        val list = mutableListOf<String>()
        children.forEach { v ->
            (v.tag as? String?)?.let { list.add(it) }
        }
        val editor = sp.edit()
        val listString = Gson().toJson(list)
        editor.putString(type, listString)
        Log.e("RabbitSurfaceView", "saveSer:$type $listString")
        editor.commit()
    }

}