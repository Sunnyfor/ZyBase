package com.sunny.zy.base.bean

import androidx.annotation.DrawableRes
import com.sunny.kit.ZyKit
import com.sunny.zy.R

/**
 * Desc 错误View的类型
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2018/8/2.
 */
class PlaceholderBean {

    constructor(desc: String, resId: Int, isGif: Boolean = false) {
        this.desc = desc
        this.resId = resId
        this.isGif = isGif
    }

    constructor()

    var desc: String = ""

    @DrawableRes
    var resId: Int = 0

    var isGif = false


    fun setEmptyData(msg: String? = null): PlaceholderBean {
        desc = msg ?: ZyKit.getContext().getString(R.string.emptyData)
        resId = R.drawable.zy_svg_empty_data
        return this
    }

    fun setNetworkError(msg: String? = null): PlaceholderBean {
        desc = msg ?: ZyKit.getContext().getString(R.string.networkError)
        resId = R.drawable.zy_svg_network_error
        return this
    }

    fun setOtherError(msg: String? = null): PlaceholderBean {
        desc = msg ?: ZyKit.getContext().getString(R.string.otherError)
        resId = R.drawable.zy_svg_other_error
        return this
    }
}