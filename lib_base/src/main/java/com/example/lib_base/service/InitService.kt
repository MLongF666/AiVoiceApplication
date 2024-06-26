package com.example.lib_base.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtil
import com.example.lib_voice.manager.VoiceManager

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/6/25 9:31
 * @version: 1.0
 */
class InitService : IntentService(InitService::class.simpleName) {
    override fun onCreate() {
        super.onCreate()
        L.i("初始化开始")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(999, NotificationHelper.bindInitService("正在运行"))
//        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onHandleIntent(intent: Intent?) {
        SpUtil.init(this)
        L.i("执行初始化操作")
    }

    override fun onDestroy() {
        super.onDestroy()
        L.i("初始化完成")

    }
}