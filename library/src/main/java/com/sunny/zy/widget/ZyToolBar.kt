package com.sunny.zy.widget

import android.app.Activity
import android.content.Context
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
import com.sunny.zy.base.bean.MenuBean
import com.sunny.zy.config.ZyBaseConfig

class ZyToolBar : FrameLayout {
    private var defaultRes = R.layout.zy_layout_title
    private var layoutRes = 0
    private var titleView: View? = null
    var toolbarHeight = 0 //设置标题栏高度

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun <T> getView(@IdRes id: Int): T? {
        return titleView?.findViewById(id)
    }

    fun getTitle(): TextView? = getView<TextView>(R.id.tvTitle)

    fun setBackTitle(
        @DrawableRes backIcon: Int,
        backText: String,
        title: String,
        rightMenu: List<MenuBean> = arrayListOf()
    ) {
        val backMenuBean = MenuBean(backText, backIcon) {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
        backMenuBean.textSize = resources.getDimension(R.dimen.dp_15)
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
        if (toolbarHeight == 0) {
            toolbarHeight = ZyBaseConfig.toolBarHeight
        }
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, toolbarHeight)
        addView(titleView, layoutParams)
    }


    private fun initTitleView() {
        removeAllViews()
        titleView = LayoutInflater.from(context).inflate(layoutRes, this, false)
        setPadding(ZyBaseConfig.toolbarPadding, ZyBaseConfig.toolbarPadding)
    }


    fun setLeftPadding(left: Int) {
        titleView?.let {
            setPadding(left, it.paddingTop, it.paddingRight, it.paddingBottom)
        }
    }

    fun setRightPadding(right: Int) {
        titleView?.let {
            setPadding(it.paddingLeft, it.paddingTop, right, it.paddingBottom)
        }
    }

    fun setTopPadding(top: Int) {
        titleView?.let {
            setPadding(it.paddingStart, top, it.paddingRight, it.paddingBottom)
        }
    }

    fun setBottomPadding(bottom: Int) {
        titleView?.let {
            setPadding(it.paddingStart, it.paddingTop, it.paddingRight, bottom)
        }
    }

    fun setPadding(left: Int, right: Int) {
        titleView?.let {
            setPadding(left, it.paddingTop, right, it.paddingBottom)
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        titleView?.setPadding(left, top, right, bottom)
    }

    fun showTitle() {
        titleView?.visibility = View.VISIBLE
    }

    fun hideTitle() {
        titleView?.visibility = View.GONE
    }

    fun setTitleSimple(title: String, vararg menuItem: MenuBean) {
        setBackTitle(R.drawable.zy_svg_title_back, "", title, menuItem.toMutableList())
        leftTitle()
    }

    fun setTitleCenterSimple(title: String, vararg menuItem: MenuBean) {
        setBackTitle(R.drawable.zy_svg_title_back, "", title, menuItem.toMutableList())
        centerTitle()
    }


    fun setTitleDefault(title: String, vararg menuItem: MenuBean) {
        leftTitle()
        setBackTitle(
            R.drawable.zy_svg_title_back,
            "",
            title,
            menuItem.toMutableList()
        )
    }

    fun setTitleCenterDefault(title: String, vararg menuItem: MenuBean) {
        centerTitle()
        setBackTitle(
            R.drawable.zy_svg_title_back,
            "",
            title,
            menuItem.toMutableList()
        )
    }

    fun setTitleCustom(@DrawableRes layoutRes: Int) {
        getView<ZyMenuView>(R.id.zvLeft)?.removeAllViews()
        getView<ZyMenuView>(R.id.zvRight)?.removeAllViews()
        this@ZyToolBar.layoutRes = layoutRes
        initTitleView()
        addView(titleView)
    }

    fun leftTitle() {
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

    fun centerTitle() {
        val layoutParams =
            getView<TextView>(R.id.tvTitle)?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToEnd = ConstraintLayout.NO_ID
        layoutParams.marginStart = 0
    }
}