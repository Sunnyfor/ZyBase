package com.sunny.zy.widget

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sunny.zy.R
import com.sunny.zy.base.ICreateStateView
import com.sunny.zy.base.bean.ErrorViewBean

/**
 * Desc
 * Author ZY
 * Date 2022/6/20
 */
open class DefaultCreateStateView : ICreateStateView {

    override fun getLoadView(context: Context): View {
        return View.inflate(context, R.layout.zy_layout_loading, null)
    }

    override fun getErrorView(context: Context): View {
        return View.inflate(context, R.layout.zy_layout_error, null)
    }

    override fun showError(errorView: View,bean: ErrorViewBean) {
        val tvTitle = errorView.findViewById<TextView>(R.id.tvDesc)
        tvTitle.text = bean.desc
        val ivIcon = errorView.findViewById<ImageView>(R.id.ivIcon)
        if (bean.isGif) {
            Glide.with(errorView.context)
                .asGif()
                .load(bean.resId)
                .into(ivIcon)
        } else {
            ivIcon?.setImageResource(bean.resId)
        }
    }
}