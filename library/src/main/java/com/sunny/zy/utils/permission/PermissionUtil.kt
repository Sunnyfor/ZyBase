package com.sunny.zy.utils.permission

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sunny.zy.R
import com.sunny.zy.base.manager.ActivityManager
import com.sunny.zy.base.widget.dialog.ConfirmDialog

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2020/12/21 17:00
 */
class PermissionUtil(
    private var permissionResult: PermissionResult
) {

    var isCancelFinish = true

    private var isNoHint = false

    private val permissions = ArrayList<String>()

    private var launcher: ActivityResultLauncher<Array<String>>? = null

    private val activity by lazy {
        ActivityManager.getLastActivity()
    }

    var dialog: ConfirmDialog? = ConfirmDialog(activity)

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
            return
        }
        val messageSb = StringBuilder()
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
        dialog!!.setContentText = messageSb.toString()

        if (isNoHint) {
            dialog!!.setConfirmText = activity.getString(R.string.dialogAuthor)
            dialog!!.setOnConfirmListener = {
                requestPermissions(*failedPermission)
            }
        } else {
            dialog!!.setConfirmText = activity.getString(R.string.dialogSetting)
            dialog!!.setOnConfirmListener = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
        }

        dialog!!.setCancelText = activity.getString(R.string.dialogCancel)
        dialog!!.setOnCancelListener = {
            dialog!!.dismiss()
            if (isCancelFinish) {
                activity.finish()
            }
        }

        dialog!!.setCancelable(false)
        dialog!!.show()
    }

}