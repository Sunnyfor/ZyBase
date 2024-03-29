package com.sunny.zy.config

import com.sunny.kit.ZyKit
import com.sunny.kit.utils.DensityUtil
import com.sunny.zy.R
import com.sunny.zy.base.ICreateStateView
import com.sunny.zy.widget.DefaultCreateStateView

/**
 * Desc 框架全局配置清单
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2017/10/12.
 */
object ZyBaseConfig {

    /**
     * 两次点击事件间隔，单位毫秒
     */
    var clickInterval = 500L

    /**
     * 设置StatusBar文字颜色
     */
    var statusBarIsDark = false

    /**
     * 全局创建状态覆盖层View
     */
    var createStateView: ICreateStateView = DefaultCreateStateView()


    /**
     * 标题栏文字大小
     */
    var toolbarTextSize = ZyKit.getContext().resources.getDimension(R.dimen.dp_18)

}