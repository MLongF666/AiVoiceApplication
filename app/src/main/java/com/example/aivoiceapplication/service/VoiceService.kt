package com.example.aivoiceapplication.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.aivoiceapplication.R
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.WindowsHelper
import com.example.lib_base.utils.L
import com.example.lib_voice.engine.VoiceEngineAnalyze
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.wakeup.VoiceWakeUp
import org.json.JSONObject

/**
 * @description: TODO 语音服务
 * @author: mlf
 * @date: 2024/6/25 10:19
 * @version: 1.0
 */
class VoiceService : Service(), OnNluResultListener {
    private val TAG = VoiceService::class.java.simpleName
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
        initCoreVoiceService()
        super.onCreate()
    }

    private fun initCoreVoiceService() {
        WindowsHelper.initHelper(this)
        WindowsHelper.getView(R.layout.layout_windows_item)


        VoiceManager.initManager(this, object : OnAsrResultListener {
            //准备就绪
            override fun weakUpReady() {
               L.i("唤醒准备就绪")
            }

            override fun asrStartSpeak() {
                L.i("开始说话")
            }

            override fun asrStopSpeak() {
                L.i("结束说话")
            }

            override fun weakUpSuccess(result: JSONObject) {
                L.i("唤醒成功${result}")
                VoiceManager.startAsr()
            }

            override fun weakUpError(text: String) {
                L.i("唤醒失败${text}")
                //由于唤醒功能暂未成功 所以唤醒失败也开启ASR
                VoiceManager.startAsr()
            }

            override fun asrResult(result: JSONObject) {
                L.i("在线识别结果${result}")
            }

            override fun nluResult(nlu: JSONObject) {
                L.i("语义识别结果${nlu}")
                VoiceEngineAnalyze.analyzeNlu(nlu, this@VoiceService)
            }
        })
    }

    //绑定通知栏
    private fun bindNotification() {
        L.i("绑定通知栏")
        startForeground(1000,
            NotificationHelper.bindVoiceService("正在运行..."))
    }

    override fun queryWeather(city: String, date: String) {
        L.i("查询天气${city} ${date}")
    }

}