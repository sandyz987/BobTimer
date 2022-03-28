package com.sandyz.alltimers.myhome.model

import android.content.Context
import com.sandyz.alltimers.myhome.R

/**
 *@author zhangzhe
 *@date 2022/3/28
 *@description
 */

object WallpaperAndFloorModel {
    fun getWallpaper(context: Context): Int {
        val sp = context.getSharedPreferences("wallpaper_and_floor", Context.MODE_PRIVATE)
        return sp.getInt("wallpaper", R.drawable.myhome_ic_wallpaper_1)
    }

    fun getFloor(context: Context): Int {
        val sp = context.getSharedPreferences("wallpaper_and_floor", Context.MODE_PRIVATE)
        return sp.getInt("floor", R.drawable.myhome_ic_floor_1)
    }

    fun setWallpaper(context: Context, id: Int) {
        val sp = context.getSharedPreferences("wallpaper_and_floor", Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putInt("wallpaper", id)
        edit.commit()
    }

    fun setFloor(context: Context, id: Int) {
        val sp = context.getSharedPreferences("wallpaper_and_floor", Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putInt("floor", id)
        edit.commit()
    }
}