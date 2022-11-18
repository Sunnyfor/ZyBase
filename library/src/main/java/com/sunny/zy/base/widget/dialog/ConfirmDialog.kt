package com.sunny.zy.base.widget.dialog

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.sunny.zy.R

/**
 * Desc 确认对话框
 * Author ZY
 * Date 2022/11/18
 */
class ConfirmDialog(context: Context) : BaseDialog(context) {

    /**
     * 标题文字
     */
    var setTitleText = ""

    /**
     * 内容文字
     */
    var setContentText = ""

    var setContentSpannable: SpannableStringBuilder? = null

    /**
     * 左边按钮文字
     */
    var setCancelText = ""

    /**
     * 右边按钮文字
     */
    var setConfirmText = ""

    /**
     * 标题对齐方式
     */
    var setTitleGravity = Gravity.CENTER

    /**
     * 内容对齐方式
     */
    var setContentGravity = Gravity.CENTER

    var setOnConfirmListener: (() -> Unit)? = null

    var setOnCancelListener: (() -> Unit)? = null

    /**
     * 底部两个按钮是否显示
     */
    var setBtnVisible = View.VISIBLE

    /**
     * 左边按钮是否显示
     */
    var setCancelBtnVisible = View.VISIBLE

    /**
     * 右边按钮是否显示
     */
    var setConfirmBtnVisible = View.VISIBLE


    private val tvTitle by lazy {
        getView<TextView>(R.id.tvTitle)
    }

    private val tvContent by lazy {
        getView<TextView>(R.id.tvContent)
    }

    private val llBtn by lazy {
        getView<LinearLayout>(R.id.llBtn)
    }


    private val tvCancel by lazy {
        getView<TextView>(R.id.tvCancel)
    }

    private val tvConfirm by lazy {
        getView<TextView>(R.id.tvConfirm)
    }

    private val vLneW by lazy {
        getView<View>(R.id.vLneW)
    }

    private val vLneH by lazy {
        getView<View>(R.id.vLneH)
    }

    override fun initLayout() = R.layout.zy_dialog_confirm

    override fun initView() {}

    private fun initText() {
        if (setTitleText.isEmpty()) {
            setTitleText = context.resources.getString(R.string.dialogTitle)
        }

        if (setCancelText.isEmpty()) {
            setCancelText = context.resources.getString(R.string.dialogCancel)
        }

        if (setConfirmText.isEmpty()) {
            setConfirmText = context.resources.getString(R.string.dialogConfirm)
        }

        tvTitle.text = setTitleText
        tvTitle.gravity = setTitleGravity

        tvContent.visibility = View.GONE
        tvContent.gravity = setContentGravity

        if (setContentSpannable == null) {
            if (setContentText.isNotEmpty()) {
                tvContent.visibility = View.VISIBLE
                tvContent.text = setContentText
            }
        } else {
            tvContent.visibility = View.VISIBLE
            tvContent.text = setContentSpannable
        }

        llBtn.visibility = setBtnVisible

        val llBtnParams = (vLneW.layoutParams as LinearLayout.LayoutParams)
        if (setBtnVisible == View.VISIBLE) {
            vLneW.visibility = View.VISIBLE
            llBtnParams.topMargin = context.resources.getDimensionPixelOffset(R.dimen.dp_25)
        } else {
            llBtnParams.topMargin = context.resources.getDimensionPixelOffset(R.dimen.dp_34)
            vLneW.visibility = View.INVISIBLE
        }

        tvCancel.text = setCancelText
        tvCancel.visibility = setCancelBtnVisible
        tvCancel.setOnClickListener {
            setOnCancelListener?.invoke()
        }

        tvConfirm.text = setConfirmText
        tvConfirm.visibility = setConfirmBtnVisible
        tvConfirm.setOnClickListener {
            setOnConfirmListener?.invoke()
        }

        if (setCancelBtnVisible == View.VISIBLE && setConfirmBtnVisible == View.VISIBLE) {
            vLneH.visibility = View.VISIBLE
        } else {
            vLneH.visibility = View.GONE
        }
    }

    override fun loadData() {}

    override fun onClickEvent(view: View) {}

    override fun onClose() {}

    override fun show() {
        super.show()
        initText()
    }
}