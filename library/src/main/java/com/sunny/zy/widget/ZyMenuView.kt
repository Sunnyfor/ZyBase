package com.sunny.zy.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sunny.zy.listener.OnClickIntervalListener
import com.sunny.zy.R
import com.sunny.zy.base.bean.MenuBean

class ZyMenuView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        orientation = HORIZONTAL
    }

    fun setMenu(menu: MenuBean) {
        setMenu(arrayListOf(menu))
    }

    fun setMenu(menus: List<MenuBean>) {
        removeAllViews()
        menus.forEach {
            val llRoot = LinearLayout(context)
            llRoot.orientation = it.orientation
            llRoot.gravity = Gravity.CENTER
            when (it.showType) {
                MenuBean.SHOW_TYPE_TEXT -> {
                    createTextView(llRoot, it)
                }
                MenuBean.SHOW_TYPE_ICON -> {
                    createImageView(llRoot, it)
                }
                MenuBean.SHOW_TYPE_ALL -> {
                    createImageView(llRoot, it)
                    createTextView(llRoot, it)
                }
            }

            llRoot.setOnClickListener(object : OnClickIntervalListener() {
                override fun onIntervalClick(view: View) {
                    it.onClickListener?.onClick(view)
                }

            })
            if (llRoot.childCount > 0) {
                val layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
                if (childCount > 0) {
                    val interval = if (it.interval == 0) MenuBean.INTERVAL else it.interval
                    layoutParams.marginStart = interval
                }
                addView(llRoot, layoutParams)
            }
        }
    }

    private fun createTextView(rootView: LinearLayout, bean: MenuBean) {
        if (bean.title.isEmpty()) {
            return
        }
        val tvTitle = TextView(context)
        tvTitle.gravity = Gravity.CENTER
        if (bean.titleColor == 0) {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        } else {
            tvTitle.setTextColor(bean.titleColor)
        }
        if (bean.textSize > 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, bean.textSize)
        }
        tvTitle.text = bean.title
        rootView.addView(tvTitle)
    }

    private fun createImageView(rootView: LinearLayout, bean: MenuBean) {
        if (bean.icon == 0) {
            return
        }
        val layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val textMargin = if (bean.textMargin == 0) MenuBean.TEXT_MARGIN else bean.textMargin
        if (bean.orientation == MenuBean.VERTICAL) {
            layoutParams.topMargin = textMargin
        } else {
            layoutParams.marginStart = textMargin
        }
        if (bean.iconWidth > 0) {
            layoutParams.width = bean.iconWidth
        }
        if (bean.iconHeight > 0) {
            layoutParams.height = bean.iconHeight
        }
        val ivIcon = ImageView(context)
        if (bean.iconWidth > 0) {
            ivIcon.maxWidth = bean.iconWidth
        }
        if (bean.iconHeight > 0) {
            ivIcon.maxHeight = bean.iconHeight
        }
        ivIcon.scaleType = ImageView.ScaleType.FIT_XY
        ivIcon.setImageResource(bean.icon)
        rootView.addView(ivIcon, layoutParams)
    }

    //容器强制水平
    override fun setOrientation(orientation: Int) {
        super.setOrientation(HORIZONTAL)
    }
}