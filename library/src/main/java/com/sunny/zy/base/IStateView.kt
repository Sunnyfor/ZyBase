package com.sunny.zy.base

import android.view.ViewGroup
import com.sunny.zy.base.bean.PlaceholderBean

/**
 * Desc 占位图抽象View
 * Author ZY
 * Date 2022/6/20
 */
interface IStateView {

    fun showLoading()

    fun hideLoading()

    fun showError(bean: PlaceholderBean)

    fun hideError()

    fun getStateViewParent(): ViewGroup
}