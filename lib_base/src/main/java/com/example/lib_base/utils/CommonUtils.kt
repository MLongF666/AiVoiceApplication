package com.example.lib_base.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * @description: TODO 基础工具类
 * @author: mlf
 * @date: 2024/8/1 21:32
 * @version: 1.0
 */
object CommonUtils {
    //获取主进程
    fun getProcessName(mContext: Context): String {
        val am =
            mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps =
            am.runningAppProcesses ?: return ""
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return ""
    }
}