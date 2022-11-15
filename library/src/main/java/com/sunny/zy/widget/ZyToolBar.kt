package com.sunny.zy.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.sunny.zy.R
import com.sunny.zy.base.OnTitleListener
import com.sunny.zy.base.bean.MenuBean
import com.sunny.zy.config.ZyBaseConfig

class ZyToolBar : FrameLayout, OnTitleListener {
    private var defaultRes = R.layout.zy_default_title
    private var layoutRes = 0
    private var titleView: View? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setBackgroundResource(R.color.colorPrimary)
    }

    fun <T> getView(@IdRes id: Int): T? {
        return titleView?.findViewById(id)
    }

    fun setTitleSize(px: Float) {
        getView<TextView>(R.id.tvTitle)?.setTextSize(TypedValue.COMPLEX_UNIT_PX, px)
    }


    fun setBackTitle(
        @DrawableRes backIcon: Int,
        backText: String,
        title: String,
        rightMenu: List<MenuBean>
    ) {
        val backMenuBean = MenuBean(backText, backIcon) {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
        backMenuBean.textSize = resources.getDimension(R.dimen.dp_16)
        backMenuBean.showType = MenuBean.SHOW_TYPE_ALL
        backMenuBean.orientation = MenuBean.HORIZONTAL
        setTitle(arrayListOf(backMenuBean), title, rightMenu)
    }


    fun setTitle(leftMenu: List<MenuBean>, title: String, rightMenu: List<MenuBean>) {
        layoutRes = defaultRes
        initTitleView()
        getView<ZyMenuView>(R.id.zvLeft)?.setMenu(leftMenu)
        getView<TextView>(R.id.tvTitle)?.let {
            it.text = title
            if (ZyBaseConfig.toolbarTextSize > 0) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, ZyBaseConfig.toolbarTextSize)
            }
        }
        getView<ZyMenuView>(R.id.zvRight)?.setMenu(rightMenu)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ZyBaseConfig.toolBarHeight)
        addView(titleView, layoutParams)
    }


    private fun initTitleView() {
        removeAllViews()
        titleView = LayoutInflater.from(context).inflate(layoutRes, this, false)
        titleView?.setPadding(ZyBaseConfig.toolbarPadding, 0, ZyBaseConfig.toolbarPadding, 0)
    }

    override fun showTitle() {
        titleView?.visibility = View.VISIBLE
    }

    override fun hideTitle() {
        titleView?.visibility = View.GONE
    }

    override fun setTitleSimple(title: String, vararg menuItem: MenuBean) {
        setBackTitle(R.drawable.svg_title_back, "", title, menuItem.toMutableList())
        leftTitle()
    }

    override fun setTitleCenterSimple(title: String, vararg menuItem: MenuBean) {
        setBackTitle(R.drawable.svg_title_back, "返回", title, menuItem.toMutableList())
        centerTitle()
    }


    override fun setTitleDefault(title: String, vararg menuItem: MenuBean) {
        leftTitle()
        setBackTitle(
            R.drawable.svg_title_back,
            "",
            title,
            menuItem.toMutableList()
        )
    }

    override fun setTitleCenterDefault(title: String, vararg menuItem: MenuBean) {
        centerTitle()
        setBackTitle(
            R.drawable.svg_title_back,
            "",
            title,
            menuItem.toMutableList()
        )
    }

    override fun setTitleCustom(@DrawableRes layoutRes: Int) {
        getView<ZyMenuView>(R.id.zvLeft)?.removeAllViews()
        getView<ZyMenuView>(R.id.zvRight)?.removeAllViews()
        this@ZyToolBar.layoutRes = layoutRes
        initTitleView()
        addView(titleView)
    }

    private fun leftTitle() {
        val layoutParams =
            getView<TextView>(R.id.tvTitle)?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.startToStart = ConstraintLayout.NO_ID
        layoutParams.endToEnd = ConstraintLayout.NO_ID
        layoutParams.startToEnd = R.id.zvLeft
        val visibility = getView<View>(R.id.zvLeft)?.visibility

        if (visibility == View.VISIBLE) {
            layoutParams.marginStart = ZyBaseConfig.toolbarPadding
        } else {
            layoutParams.marginStart = 0
        }
    }

    private fun centerTitle() {
        val layoutParams =
            getView<TextView>(R.id.tvTitle)?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToEnd = ConstraintLayout.NO_ID
        layoutParams.marginStart = 0
    }
}