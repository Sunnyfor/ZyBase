package com.sunny.zy.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.sunny.kit.utils.DensityUtil
import com.sunny.zy.R
import com.sunny.zy.base.bean.PlaceholderBean
import com.sunny.zy.base.manager.ActivityManager
import com.sunny.zy.config.ZyBaseConfig
import com.sunny.zy.listener.OnClickIntervalListener
import com.sunny.zy.widget.DefaultStateView
import com.sunny.zy.widget.ZyToolBar


/**
 * Desc Activity基类
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2018/8/2
 */
abstract class BaseActivity : AppCompatActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback, IBaseView,
    View.OnClickListener {

    open var taskTag = "DefaultActivity"

    open var savedInstanceState: Bundle? = null

    private var isDark = false

    private var mStatusBarColor: Int = R.color.colorPrimary

    //限制点击间隔
    private val onClickIntervalListener by lazy {
        object : OnClickIntervalListener() {
            override fun onIntervalClick(view: View) {
                onClickEvent(view)
            }
        }
    }

    private val vTopBg: View by lazy {
        getView(R.id.vTopBg)
    }

    open val toolbar: ZyToolBar by lazy {
        getView(R.id.zyToolBar)
    }


    open val statusBar: View by lazy {
        getView(R.id.vStatusBar)
    }

    open val parentView: FrameLayout by lazy {
        getView(R.id.flContent)
    }

    open val defaultStateView: DefaultStateView by lazy {
        object : DefaultStateView(ZyBaseConfig.createStateView) {
            override fun getStateViewParent(): ViewGroup {
                return this@BaseActivity.getStateViewParent()
            }
        }
    }

    //屏幕方向
    open var screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        requestedOrientation = screenOrientation //强制屏幕
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        setContentView(R.layout.zy_activity_base)
        statusBar.setBackgroundResource(mStatusBarColor)
        toolbar.setBackgroundResource(mStatusBarColor)
        statusBar.layoutParams.height = DensityUtil.getStatusBarHeight()
        when (val layoutView = initLayout()) {
            is Int -> {
                if (layoutView != 0) {
                    parentView.addView(layoutInflater.inflate(layoutView, parentView, false))
                }
            }
            is View -> {
                parentView.addView(layoutView)
            }

            is Fragment -> {
                supportFragmentManager.beginTransaction()
                    .add(parentView.id, layoutView).commit()
            }
        }
        setStatusBarIsDark(ZyBaseConfig.statusBarIsDark)
        ActivityManager.addActivity(this)
        initView()
        loadData()
    }


    override fun onDestroy() {
        ActivityManager.removeActivity(this)
        onClose()
        super.onDestroy()
    }

    /**
     * 显示loading覆盖层
     */
    override fun showLoading() {
        defaultStateView.showLoading()
    }

    /**
     * 隐藏loading覆盖层
     */
    override fun hideLoading() {
        defaultStateView.hideLoading()
    }

    /**
     * 显示错误覆盖层
     */
    override fun showError(bean: PlaceholderBean) {
        defaultStateView.showError(bean)
    }

    /**
     * 隐藏错误覆盖层
     */
    override fun hideError() {
        defaultStateView.hideError()
    }


    /**
     * 状态覆盖层容器
     */
    override fun getStateViewParent(): ViewGroup {
        return parentView
    }

    /**
     * 批量注册点击事件
     * @param views 注册事件的View
     */
    fun setOnClickListener(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this)
        }
    }


    override fun onClick(view: View) {
        onClickIntervalListener.onClick(view)
    }


    fun <T : View> getView(@IdRes id: Int): T {
        return findViewById(id)
    }

    /**
     * 设置沉浸式背景
     */
    fun setImmersionResource(@DrawableRes res: Int, height: Int = 0) {
        immersionBgSetting(height) {
            vTopBg.setBackgroundResource(res)
        }

    }

    fun setImmersionColor(@ColorInt color: Int, height: Int = 0) {
        immersionBgSetting(height) {
            vTopBg.setBackgroundColor(color)
        }
    }

    /**
     * 设置沉浸式背景
     */
    fun setImmersionBitmap(bitmap: Bitmap, height: Int = 0) {
        immersionBgSetting(height) {
            vTopBg.background = bitmap.toDrawable(resources)
        }
    }

    /**
     * 计算沉浸式背景高度
     */
    private fun immersionBgSetting(height: Int, callback: () -> Unit) {
        statusBar.setBackgroundResource(android.R.color.transparent)
        toolbar.setBackgroundResource(android.R.color.transparent)
        val titleView = toolbar[0]
        titleView.post {
            if (height == 0) {
                var toolbarHeight = 0
                if (toolbar.visibility == View.VISIBLE) {
                    toolbarHeight = titleView.height
                }
                vTopBg.layoutParams.height =
                    toolbarHeight + DensityUtil.getStatusBarHeight()
            } else {
                vTopBg.layoutParams.height = height
            }
            callback.invoke()
        }
    }

    /**
     * 设置状态栏文字颜色
     *  @param isDark true为黑色 false为白色
     */
    @Suppress("DEPRECATION")
    fun setStatusBarIsDark(isDark: Boolean) {
        this.isDark = isDark
        window.decorView.systemUiVisibility = if (isDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        } else {
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }


    fun showStatusBar() {
        statusBar.visibility = View.VISIBLE
    }

    fun hideStatusBar() {
        statusBar.visibility = View.GONE
    }

    /**
     * 显示输入法键盘
     */
    fun showKeyboard(editText: EditText, delayMillis: Long = 100) {
        editText.postDelayed({
            editText.requestFocus()
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        }, delayMillis)
    }

    /**
     * 隐藏输入法键盘
     */
    fun hideKeyboard() {
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    /**
     * fragment加载完成后进行回调
     */
    open fun onFragmentLoadFinish(fragment: Fragment) {}

}