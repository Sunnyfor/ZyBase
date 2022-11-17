package com.sunny.zy.base.widget.dialog

import android.content.Context
import android.view.View
import android.widget.TextView

abstract class BasePermissionsDialog(context: Context) : BaseDialog(context) {

    var setTitle = "提醒"

    var setSetting = "设置"

    var setAuthor = "授权"

    var setCancel = "取消"

    var setPositiveCallBack: (() -> Unit)? = null

    var setNegativeCallBack: (() -> Unit)? = null

    /**
     * 通过此方法自定义message文案
     */
    var setMessageCallBack: ((Array<String>) -> String)? = null

    //确定组件
    abstract fun getPositiveTextView(): TextView

    //取消组件
    abstract fun getNegativeTextView(): TextView

    //消息组件
    abstract fun getMessageTextView(): TextView

    //标题组件
    abstract fun getTitleTextView(): TextView

    override fun initView() {
        getNegativeTextView().setOnClickListener {
            setNegativeCallBack?.invoke()
        }

        getPositiveTextView().setOnClickListener {
            setPositiveCallBack?.invoke()
        }
    }

    override fun loadData() {}

    override fun onClickEvent(view: View) {}



    override fun onClose() {}
}