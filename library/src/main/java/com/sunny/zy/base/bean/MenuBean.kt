package com.sunny.zy.base.bean

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.sunny.kit.ZyKit
import com.sunny.zy.R

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2020/10/29 17:27
 */
class MenuBean {

    companion object {
        //
        const val SHOW_TYPE_TEXT = 1
        const val SHOW_TYPE_ICON = 2
        const val SHOW_TYPE_ALL = 0

        //排列方式
        const val HORIZONTAL = 0
        const val VERTICAL = 1

        //全局设置 文字与图标之间的间隔
        var TEXT_MARGIN: Int = ZyKit.getContext().resources.getDimensionPixelSize(R.dimen.dp_4)

        //全局设置 菜单之间的间隔
        var INTERVAL: Int = ZyKit.getContext().resources.getDimensionPixelSize(R.dimen.dp_6)
    }

    constructor()

    constructor(@DrawableRes icon: Int, onClickListener: View.OnClickListener) {
        this.icon = icon
        this.onClickListener = onClickListener
    }

    constructor(title: String, onClickListener: View.OnClickListener) {
        this.title = title
        this.onClickListener = onClickListener
    }


    constructor(@DrawableRes icon: Int, title: String, onClickListener: View.OnClickListener) {
        this.title = title
        this.icon = icon
        this.onClickListener = onClickListener
    }

    constructor(title: String, @DrawableRes icon: Int, onClickListener: View.OnClickListener) {
        this.title = title
        this.icon = icon
        this.onClickListener = onClickListener
    }

    var title: String = ""

    @DrawableRes
    var icon: Int = 0

    /**
     * 图片宽度
     */
    var iconWidth = 0

    /***
     *  图标高度
     */
    var iconHeight = 0

    /**
     * 显示模式（只显示文字、只显示图标、显示所有）
     */
    var showType = SHOW_TYPE_ALL

    /**
     * 文字颜色
     */
    @ColorInt
    var titleColor: Int = 0

    /**
     * 文字大小
     */
    var textSize = 0F

    /**
     * 文字与图标之间的间隔（仅所有模式下生效）
     */
    var textMargin = 0


    /**
     * 图文排列方向
     */
    var orientation = HORIZONTAL
    var onClickListener: View.OnClickListener? = null
}