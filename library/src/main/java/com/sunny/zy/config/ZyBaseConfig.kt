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
     * 设置StatusBar文字颜色
     */
    var statusBarIsDark = false

    /**
     * 全局创建状态覆盖层View
     */
    var createStateView: ICreateStateView = DefaultCreateStateView()

    /**
     * 设置标题栏左右填充距离（包含默认标题与左边菜单的距离）
     */
    var toolbarPadding = ZyKit.getContext().resources.getDimensionPixelOffset(R.dimen.dp_10)

    /**
     * 标题栏高度高度 0为默认
     */
    var toolBarHeight = DensityUtil.getToolBarHeight()

    /**
     * 标题栏文字大小
     */
    var toolbarTextSize = ZyKit.getContext().resources.getDimension(R.dimen.dp_18)

    /**
     * 图标大小
     */
    var toolbarIconSize = ZyKit.getContext().resources.getDimensionPixelOffset(R.dimen.dp_20)
}