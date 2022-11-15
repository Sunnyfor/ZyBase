package com.sunny.zy.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sunny.kit.ZyKit

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2020/11/12 14:53
 */
class BitmapUtil {
    private var resId = 0
    private var originalBitmap: Bitmap? = null

    fun initBitmap(@DrawableRes res: Int, width: Int, height: Int, resultCallBack: () -> Unit) {
        destroy()
        resId = res
        val drawable = AppCompatResources.getDrawable(ZyKit.getContext(), res)
        originalBitmap = drawable?.toBitmap(width, height)
        resultCallBack.invoke()
    }

    fun initBitmap(bitmap: Bitmap, width: Int, height: Int, resultCallBack: () -> Unit) {
        originalBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
        resultCallBack.invoke()
    }

    fun initBitmap(url: String, width: Int, height: Int, resultCallBack: () -> Unit) {
        destroy()
        Glide.with(ZyKit.getContext()).asBitmap().load(url)
            .into(object : CustomTarget<Bitmap>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    originalBitmap = resource
                    resultCallBack.invoke()
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    }

    fun getCroppedBitmap(x: Int, y: Int, width: Int, height: Int): Bitmap? {
        var bitmapWidget = width
        if (width == 0) {
            bitmapWidget = originalBitmap?.width ?: 0
        }
        var bitmapHeight = height
        if (height == 0) {
            bitmapHeight = (originalBitmap?.height ?: 0) - y
        }

        return Bitmap.createBitmap(originalBitmap ?: return null, x, y, bitmapWidget, bitmapHeight)
    }

    fun destroy() {
        originalBitmap?.recycle()
        originalBitmap = null
    }
}