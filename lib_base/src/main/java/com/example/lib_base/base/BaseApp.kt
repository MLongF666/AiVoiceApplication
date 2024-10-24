package com.example.lib_base.base

import android.app.Application
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.service.InitService
import com.example.lib_base.utils.CommonUtils
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtil

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //只有主进程才能初始化
        val processName = CommonUtils.getProcessName(this)
        if (!TextUtils.isEmpty(processName)) {
            if (processName == packageName) {
                initApp()
            }
        }
    }

    //初始化App
    private fun initApp() {
        ARouterHelper.initHelper(this)
        NotificationHelper.initHelper(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            L.i("start InitService android 8.0以上")
            startForegroundService(Intent(this, InitService::class.java))
        } else {
            startService(Intent(this, InitService::class.java))
        }


    }
}