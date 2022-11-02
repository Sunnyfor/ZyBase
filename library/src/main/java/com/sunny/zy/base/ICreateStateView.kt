package com.sunny.zy.base

import android.content.Context
import android.view.View
import com.sunny.zy.base.bean.ErrorViewBean

/**
 * Desc
 * Author ZY
 * Date 2022/6/20
 */
interface ICreateStateView {

    fun getLoadView(context: Context): View

    fun getErrorView(context: Context): View

    fun showError(errorView: View, bean: ErrorViewBean)
}