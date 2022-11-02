package com.sunny.zy.base.manager

import com.sunny.kit.utils.LogUtil
import com.sunny.zy.ZyFrameStore
import com.sunny.zy.base.BaseActivity
import java.util.*

object ZyActivityManager {

    const val KILL = 0

    const val KEEP = 1

    private val activityStack = Stack<BaseActivity>()

    /**
     * 存储管理Activity
     */
    fun addActivity(baseActivity: BaseActivity) {
        activityStack.add(baseActivity)
    }

    /**
     * 移除Activity
     */
    fun removeActivity(baseActivity: BaseActivity) {
        activityStack.remove(baseActivity)
    }


    /**
     * 获取Activity的实例数量
     */
    fun getActivitySize(clazz: Class<*> = BaseActivity::class.java): Int {
        if (clazz != BaseActivity::class.java) {
            return activityStack.filter { it.javaClass == clazz }.size
        }
        return activityStack.size
    }

    fun getLastActivity(): BaseActivity {
        return activityStack.lastElement()
    }

    fun getFastActivity(): BaseActivity {
        return activityStack.firstElement()
    }

    /**
     * 关闭或保留指定TaskTag的Activity
     */
    fun optionTagActivity(tagActivity: BaseActivity, option: Int = KILL) {
        val removeStack = Stack<BaseActivity>()
        activityStack.forEach {
            var flag = it.taskTag == tagActivity.taskTag
            if (option == KEEP) {
                flag = it.taskTag != tagActivity.taskTag
            }
            if (flag) {
                removeStack.add(it)
                it.finish()
            }
        }
        activityStack.removeAll(removeStack)
    }

    /**
     * 关闭所有的Activity
     */
    fun finishAllActivity() {
        activityStack.forEach {
            it.finish()
        }
        activityStack.clear()
    }

    /**
     * 获取指定Class的Activity实例
     * @return 如果存在的话返回具体实例用于调用内部方法
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseActivity> getActivity(clazz: Class<T>, position: Int? = null): T? {
        val result = activityStack.filter { it.javaClass == clazz }
        if (result.isNotEmpty()) {
            val index = position ?: (result.size - 1)
            if (index < result.size) {
                return result[index] as T
            }
        }
        return null
    }
}