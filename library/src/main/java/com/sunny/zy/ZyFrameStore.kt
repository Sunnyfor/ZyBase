package com.sunny.zy

import java.util.*

/**
 * Desc 内存数据存储（回收恢复数据待优化）
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2018/08/02.
 */
object ZyFrameStore {

    private val storeMap = HashMap<String, Any>() //内存数据存储

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String, isDelete: Boolean = false): T? {

        if (!storeMap.containsKey(key)) {
            return null
        }

        val result = storeMap[key]

        if (isDelete) {
            removeData(key)
        }
        return result as T
    }


    /**
     * 存储数据
     */
    fun setData(key: String, t: Any?) {
        if (t != null) {
            storeMap[key] = t
        }
    }

    /**
     * 删除数据
     */
    fun removeData(key: String) {
        storeMap.remove(key)
    }

    /**
     * 清空所有内存数据
     */
    fun removeAllData() {
        storeMap.clear()
    }
}