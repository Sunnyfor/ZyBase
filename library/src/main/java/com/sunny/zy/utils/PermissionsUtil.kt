package com.sunny.zy.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.sunny.kit.utils.SpUtil
import com.sunny.zy.base.manager.ZyActivityManager

/**
 * Desc
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2020/12/21 17:00
 */
class PermissionsUtil(var permissionResult: PermissionResult) {

    var isCancelFinish = false

    var isNoHintFinish = false

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
                    permissionResult.onPermissionSuccess(successList)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        isNoHint = !failedList.all {
                            activity.shouldShowRequestPermissionRationale(it)
                        }
                    }
                    SpUtil.get().setBoolean("isNoHint", isNoHint)
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
            val failedList = arrayListOf<String>()
            permissions.forEach {
                if (activity.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
                ) {
                    failedList.add(it)
                }
            }
            if (failedList.isEmpty()) {
                isNoHint = false
                SpUtil.get().remove("isNoHint")
                permissionResult.onPermissionSuccess(permissions)
            } else {
                isNoHint = SpUtil.get().getBoolean("isNoHint")
                if (isNoHint) {
                    showSettingPermissionDialog(
                        Array(failedList.size) { failedList[it] }
                    )
                } else {
                    launcher?.launch(Array(permissions.size) { permissions[it] })
                }
            }
        } else {
            permissionResult.onPermissionSuccess(permissions)
        }
    }


    private fun showSettingPermissionDialog(
        failedPermission: Array<String>
    ) {
        val build = AlertDialog.Builder(activity)
        build.setTitle("帮助")
        val messageSb = StringBuilder()
        val pm = activity.packageManager
        failedPermission.forEach {
            val permissionInfo = pm.getPermissionInfo(it, 0)
            val permissionName = permissionInfo.loadLabel(pm)
            if (!messageSb.contains(permissionName)) {
                messageSb.append("【").append(permissionName).append("】").append(" ")
            }
        }
        build.setMessage("当前应用缺少${messageSb}权限,此功能无法正常使用！")
        if (isNoHint) {
            build.setPositiveButton("设置") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
        } else {
            build.setPositiveButton("授权") { _, _ ->
                requestPermissions(*failedPermission)
            }
        }
        build.setNegativeButton("取消") { _, _ ->
            if (isCancelFinish) {
                activity.finish()
            }

            if (isNoHint && isNoHintFinish) {
                activity.finish()
            }
        }
        build.setCancelable(false)
        build.show()
    }

    interface PermissionResult {
        fun onPermissionSuccess(permissions: List<String>)
        fun onPermissionFailed(permissions: List<String>)
    }
}