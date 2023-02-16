package com.sunny.zy

import com.sunny.kit.utils.SpUtil
import java.util.*

/**
 * Desc 内存数据存储（回收恢复数据待优化）
 * Author ZY
 * Mail sunnyfor98@gmail.com
 * Date 2018/08/02.
 */
object ZyFrameStore {

    private val storeMap = HashMap<String, Any>() //内存数据存储

    private const val CLAZZ_FILE = ".class"
    private const val VALUE_FILE = ".value"

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String, isDelete: Boolean = false): T? {

        if (!storeMap.containsKey(key)) {
            val clazzName = SpUtil.get(CLAZZ_FILE).getString(key)
            if (clazzName.isNotEmpty()) {
                val clazz = Class.forName(clazzName)
                val value = SpUtil.get(VALUE_FILE).getObject<T>(key, clazz) ?: return null
                storeMap[key] = value
            } else {
                return null
            }
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
            SpUtil.get(VALUE_FILE).setObject(key, t)
            SpUtil.get(CLAZZ_FILE).setString(key, t.javaClass.name)
        }
    }

    /**
     * 删除数据
     */
    fun removeData(key: String) {
        storeMap.remove(key)
        SpUtil.get(CLAZZ_FILE).remove(key)
        SpUtil.get(VALUE_FILE).remove(key)
    }

    /**
     * 清空所有内存数据
     */
    fun removeAllData() {
        storeMap.entries.forEach {
            removeData(it.key)
        }
    }
}