package com.sandyz.alltimers.common.service

import com.alibaba.android.arouter.launcher.ARouter

object ServiceManager {
    fun <T> getService(serviceClass: Class<T>): T = ARouter.getInstance().navigation(serviceClass)

    @Suppress("UNCHECKED_CAST")
    fun <T> getService(servicePath: String) = ARouter.getInstance().build(servicePath).navigation() as T
}