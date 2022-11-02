package com.sunny.zy.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sunny.kit.listener.OnClickIntervalListener
import com.sunny.zy.R
import com.sunny.zy.config.ZyBaseConfig
import com.sunny.zy.base.IBaseView
import com.sunny.zy.base.bean.ErrorViewBean
import com.sunny.zy.widget.DefaultStateView

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2022/3/1 15:07
 */
abstract class BaseDialog(context: Context) : Dialog(context), IBaseView, View.OnClickListener {

    private val flParentView by lazy {
        FrameLayout(context)
    }

    private val onClickIntervalListener by lazy {
        object : OnClickIntervalListener() {
            override fun onIntervalClick(view: View) {
                onClickEvent(view)
            }
        }
    }

    init {
        // 解决页面被后台回收后，dialog 窗体泄漏
        if (context is ContextWrapper) {
            if (context.baseContext is FragmentActivity) {
                val activity = context.baseContext as FragmentActivity
                activity.lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy() {
                        dismiss()
                    }
                })
            }
        }
    }

    open val defaultStateView: DefaultStateView by lazy {
        object : DefaultStateView(ZyBaseConfig.createStateView) {
            override fun getStateViewParent(): ViewGroup {
                return this@BaseDialog.getStateViewParent()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (val initRes = initLayout()) {
            is View -> {
                flParentView.addView(initRes)
            }
            is Int -> {
                flParentView.addView(
                    LayoutInflater.from(context).inflate(initRes, flParentView, false)
                )
            }
        }
        setContentView(flParentView)
//        setCancelable(false)
//        setCanceledOnTouchOutside(false)
//        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
        loadData()
    }


    /**
     * 批量注册点击事件
     * @param views 注册事件的View
     */
    fun setOnClickListener(vararg views: View) {
        setOnClickListener(this, *views)
    }

    fun setOnClickListener(onClick: View.OnClickListener, vararg views: View) {
        views.forEach {
            it.setOnClickListener(onClick)
        }
    }


    override fun showLoading() {
        defaultStateView.showLoading()
    }

    override fun hideLoading() {
        defaultStateView.hideLoading()
    }

    override fun showError(bean: ErrorViewBean) {
        defaultStateView.showError(bean)
    }

    override fun hideError() {
        defaultStateView.hideLoading()
    }


    override fun onClick(v: View) {
        onClickIntervalListener.onClick(v)
    }

    override fun getStateViewParent(): ViewGroup {
        return flParentView
    }
}