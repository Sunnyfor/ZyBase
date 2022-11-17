package com.sunny.zy.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sunny.kit.utils.LogUtil
import com.sunny.zy.R
import com.sunny.zy.base.manager.ZyActivityManager
import com.sunny.zy.base.widget.dialog.BasePermissionsDialog

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2020/12/21 17:00
 */
class PermissionsUtil(
    private var permissionResult: PermissionResult,
    private var dialog: BasePermissionsDialog? = null
) {

    var isCancelFinish = true

    private var isNoHint = false

    private val permissions = ArrayList<String>()

    private var launcher: ActivityResultLauncher<Array<String>>? = null

    private val activity by lazy {
        ZyActivityManager.getLastActivity()
    }

    init {
        launcher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
                val successList = arrayListOf<String>()
                val failedList = arrayListOf<String>()
                resultMap.entries.forEach { entry ->
                    if (entry.value) {
                        successList.add(entry.key)
                    } else {
                        failedList.add(entry.key)
                    }
                }
                if (failedList.isEmpty()) {
                    dialog?.dismiss()
                    permissionResult.onPermissionSuccess(successList)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        isNoHint = failedList.all {
                            activity.shouldShowRequestPermissionRationale(it)
                        }
                        LogUtil.i("权限是否拒绝:$isNoHint")
                    }
                    showSettingPermissionDialog(Array(failedList.size) { failedList[it] })
                    permissionResult.onPermissionFailed(failedList)
                }
            }
    }

    fun requestPermissions(vararg permission: String) {
        if (permission.isNotEmpty()) {
            permissions.clear()
            permissions.addAll(permission)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            launcher?.launch(Array(permissions.size) { permissions[it] })
        } else {
            permissionResult.onPermissionSuccess(permissions)
        }
    }


    private fun showSettingPermissionDialog(
        failedPermission: Array<String>
    ) {
        if (dialog == null) {
            initDefaultDialog()
        }
        dialog?.let {
            it.setCancelable(false)
            it.show()

            it.getTitleTextView().text = it.setTitle
            val messageSb = StringBuilder()
            if (it.setMessageCallBack != null) {
                messageSb.append(it.setMessageCallBack?.invoke(failedPermission))
            } else {
                val pm = activity.packageManager
                messageSb.append("当前应用缺少")
                failedPermission.forEach { permission ->
                    val permissionInfo = pm.getPermissionInfo(permission, 0)
                    val permissionName = permissionInfo.loadLabel(pm)
                    if (!messageSb.contains(permissionName)) {
                        messageSb.append("【").append(permissionName).append("】")
                    }
                }
                messageSb.append("权限，此功能无法正常使用！")
            }
            it.getMessageTextView().text = messageSb


            if (isNoHint) {
                it.getPositiveTextView().text = it.setAuthor
                it.setPositiveCallBack = {
                    requestPermissions(*failedPermission)
                }
            } else {
                it.getPositiveTextView().text = it.setSetting
                it.setPositiveCallBack = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                }
            }

            it.getNegativeTextView().text = it.setCancel
            it.setNegativeCallBack = {
                it.dismiss()
                if (isCancelFinish) {
                    activity.finish()
                }
            }
        }
    }


    private fun initDefaultDialog() {
        dialog = object : BasePermissionsDialog(activity) {

            override fun initLayout() = R.layout.zy_dialog_confirm

            override fun getPositiveTextView(): TextView {
                return getView(R.id.tvConfirm)
            }

            override fun getNegativeTextView(): TextView {
                return getView(R.id.tvCancel)
            }

            override fun getMessageTextView(): TextView {
                return getView(R.id.tvMessage)
            }

            override fun getTitleTextView(): TextView {
                return getView(R.id.tvTitle)
            }
        }
    }

    interface PermissionResult {
        fun onPermissionSuccess(permissions: List<String>)
        fun onPermissionFailed(permissions: List<String>)
    }
}