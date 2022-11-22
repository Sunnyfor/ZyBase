package com.sunny.zy.listener

import android.view.View
import android.view.View.OnClickListener
import com.sunny.kit.R
import com.sunny.kit.utils.LogUtil
import com.sunny.zy.config.ZyBaseConfig

/**
 * Desc 防止多次点击
 * Author ZY
 * Date 2022/8/3
 */
abstract class OnClickIntervalListener : OnClickListener {

    /**
     * 点击事件处理
     */
    private var lastClickId = 0
    private var lastClickTime = 0L

    /**
     * 检测是否多次点击？true为多次点击
     */
    override fun onClick(view: View) {
        var tag = view.getTag(R.id.clickInterval)
        if (tag == null) {
            tag = ZyBaseConfig.clickInterval
            view.setTag(R.id.clickInterval, tag)
        }
        val interval = when (tag) {
            is Long -> {
                tag
            }
            is Int -> {
                tag.toLong()
            }
            else -> {
                0
            }
        }

        if (view.id == lastClickId) {
            val time = System.currentTimeMillis() - lastClickTime
            if (time < interval) {
                LogUtil.i("拦截重复点击事件，剩余时间：[${interval - time}] 毫秒")
                return
            }
        }
        lastClickId = view.id
        lastClickTime = System.currentTimeMillis()
        onIntervalClick(view)
    }

    abstract fun onIntervalClick(view: View)
}