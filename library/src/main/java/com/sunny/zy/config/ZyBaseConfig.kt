package com.sunny.zy.config

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
     * 设置StatusBar文字颜色
     */
    var statusBarIsDark = false

    /**
     * 全局创建状态覆盖层View
     */
    var createStateView: ICreateStateView = DefaultCreateStateView()
}