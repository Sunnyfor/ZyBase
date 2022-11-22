package com.sunny.zy.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.sunny.zy.listener.OnClickIntervalListener
import com.sunny.zy.base.bean.PlaceholderBean
import com.sunny.zy.config.ZyBaseConfig
import com.sunny.zy.widget.DefaultStateView
import com.sunny.zy.widget.ZyToolBar


/**
 * Desc Fragment基类
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2018/8/2
 */
abstract class BaseFragment : Fragment(), IBaseView, View.OnClickListener {
    private var savedInstanceState: Bundle? = null

    val toolbar: ZyToolBar
        get() = getBaseActivity().toolbar

    private val flParentView by lazy {
        FrameLayout(requireContext())
    }

    private val onClickIntervalListener by lazy {
        object : OnClickIntervalListener() {
            override fun onIntervalClick(view: View) {
                onClickEvent(view)
            }
        }
    }

    open val defaultStateView: DefaultStateView by lazy {
        object : DefaultStateView(ZyBaseConfig.createStateView) {
            override fun getStateViewParent(): ViewGroup {
                return this@BaseFragment.getStateViewParent()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.savedInstanceState = savedInstanceState

        val layoutView = initLayout()

        if (layoutView is Int) {
            flParentView.addView(inflater.inflate(layoutView, container, false))
        }

        if (layoutView is View) {
            flParentView.addView(layoutView)
        }

        return flParentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData()
        getBaseActivity().onFragmentLoadFinish(this)
    }


    open fun getBaseActivity(): BaseActivity = requireActivity() as BaseActivity

    /**
     * 批量注册点击事件
     * @param views 注册事件的View
     */
    open fun setOnClickListener(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun showLoading() {
        defaultStateView.showLoading()
    }

    override fun hideLoading() {
        defaultStateView.hideLoading()
    }

    override fun showError(bean: PlaceholderBean) {
        defaultStateView.showError(bean)
    }

    override fun hideError() {
        defaultStateView.hideError()
    }


    override fun getStateViewParent(): ViewGroup {
        return flParentView
    }


    override fun onClick(v: View) {
        onClickIntervalListener.onIntervalClick(v)
    }



    fun setStatusBarTextModel(isDark: Boolean) {
        getBaseActivity().setStatusBarModel(isDark)
    }

    fun showStatusBar() {
        getBaseActivity().showStatusBar()
    }

    fun hideStatusBar() {
        getBaseActivity().hideStatusBar()
    }

    fun <T : View> findViewById(@IdRes id: Int): T {
        return flParentView.findViewById(id)
    }

    override fun onDestroy() {
        onClose()
        super.onDestroy()
    }
}