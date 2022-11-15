package com.sunny.zy.base

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.sunny.zy.base.bean.MenuBean

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2020/11/6 17:35
 */
interface OnTitleListener {

    fun showTitle()

    fun hideTitle()

    fun setTitleSimple(title: String, vararg menuItem: MenuBean)

    fun setTitleCenterSimple(title: String, vararg menuItem: MenuBean)

    fun setTitleDefault(title: String, vararg menuItem: MenuBean)

    fun setTitleCenterDefault(title: String, vararg menuItem: MenuBean)

    fun setTitleCustom(layoutRes: Int)
}