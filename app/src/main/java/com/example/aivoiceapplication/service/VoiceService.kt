package com.example.aivoiceapplication.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.utils.L
import com.example.lib_voice.manager.VoiceManager

/**
 * @description: TODO 语音服务
 * @author: mlf
 * @date: 2024/6/25 10:19
 * @version: 1.0
 */
class VoiceService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //START_STICKY 保证服务一直运行，直到被系统杀死
    //START_NOT_STICKY 默认值，服务被系统杀死，不会重启
    //START_REDELIVER_INTENT 重启服务，但会保留最后一次的Intent
    //STOP_FOREGROUND_SERVICE 停止前台服务
    //START_STICKY_COMPATIBILITY 兼容模式，保证服务一直运行，直到被系统杀死，但不保证服务被杀死时能及时停止
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        L.i("启动服务 on start command")
        bindNotification()
        return START_STICKY_COMPATIBILITY
    }

    override fun onCreate() {
        initService()
        super.onCreate()
    }

    private fun initService() {
        VoiceManager.initManager(this)
    }

    //绑定通知栏
    private fun bindNotification() {
        L.i("绑定通知栏")
        startForeground(1000,
            NotificationHelper.bindVoiceService("正在运行..."))
    }

}