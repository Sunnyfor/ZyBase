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
import androidx.core.view.setPadding
import com.sunny.zy.R
import com.sunny.zy.base.bean.MenuBean
import com.sunny.zy.listener.OnClickIntervalListener

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
        gravity = Gravity.CENTER_VERTICAL
    }

    fun setMenu(menu: MenuBean) {
        setMenu(arrayListOf(menu))
    }

    fun setMenu(menus: List<MenuBean>) {
        removeAllViews()
        menus.forEach {
            val llRoot = LinearLayout(context)
            llRoot.setPadding(resources.getDimensionPixelOffset(R.dimen.dp_10))
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

        if (bean.iconWidth > 0) {
            layoutParams.width = bean.iconWidth
        }
        if (bean.iconHeight > 0) {
            layoutParams.height = bean.iconHeight
        }
        val ivIcon = ImageView(context)
        ivIcon.scaleType = ImageView.ScaleType.FIT_XY
        ivIcon.setImageResource(bean.icon)
        if (bean.showType == MenuBean.SHOW_TYPE_ALL){
            if (bean.orientation == MenuBean.VERTICAL) {
                layoutParams.bottomMargin = textMargin
            }else{
                layoutParams.rightMargin = textMargin
            }
        }
        rootView.addView(ivIcon, layoutParams)
    }
}