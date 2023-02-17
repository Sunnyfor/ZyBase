package com.sunny.zy.base

import android.content.Context
import android.view.View
import com.sunny.zy.base.bean.PlaceholderBean

/**
 * Desc
 * Author ZY
 * Date 2022/6/20
 */
interface ICreateStateView {

    fun getLoadView(context: Context): View

    fun getPlaceholderView(context: Context): View

    fun showPlaceholder(placeholderView: View, bean: PlaceholderBean)
}