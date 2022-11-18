package com.sunny.zy.utils.permission

interface PermissionResult {
    fun onPermissionSuccess(successPermissions: List<String>)
    fun onPermissionFailed(failedPermissions: List<String>)
}