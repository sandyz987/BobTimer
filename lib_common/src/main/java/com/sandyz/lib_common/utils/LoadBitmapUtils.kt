package com.sandyz.lib_common.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.IdRes


class LoadBitmapUtils {
    fun decodeBitmapFromResource(
        res: Resources?,
        @IdRes resId: Int,
        decodeWidth: Int,
        decodeHeight: Int
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //预加载
        BitmapFactory.decodeResource(res, resId, options)
        val imgWidth = options.outWidth //要加载的图片的宽
        val imgHeight = options.outHeight //要加载的图片的高
        var inSampleSize = 1
        if (imgWidth > decodeWidth || imgHeight > decodeHeight) {
            val halfWidth = imgWidth / 2
            val halfHeight = imgHeight / 2
            while (halfWidth / inSampleSize >= decodeWidth &&
                halfHeight / inSampleSize >= decodeHeight
            ) {
                inSampleSize *= 2
            }
        }
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        return BitmapFactory.decodeResource(res, resId, options)
    }
}