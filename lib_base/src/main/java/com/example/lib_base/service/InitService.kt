package com.example.lib_base.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.SoundPoolHelper
import com.example.lib_base.helper.`fun`.AppHelper
import com.example.lib_base.helper.`fun`.CommonSettingHelper
import com.example.lib_base.helper.`fun`.ConsTellHelper
import com.example.lib_base.map.BDMapManager
import com.example.lib_base.map.GDMapManager
import com.example.lib_base.utils.AssetUtils
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtil
import com.example.lib_voice.words.WordsTools

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/6/25 9:31
 * @version: 1.0
 */

class InitService : IntentService(InitService::class.simpleName) {
    @Deprecated("Deprecated in Java")
    override fun onCreate() {
        super.onCreate()
        L.i("初始化开始")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(999, NotificationHelper.bindInitService("正在运行"))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        SpUtil.init(this)
        WordsTools.initTools(this)
        SoundPoolHelper.init(this)
        AppHelper.initHelper(this)
        CommonSettingHelper.initHelper(this)
        ConsTellHelper.initHelper(this)
        AssetUtils.initUtils(this)

        L.i("执行初始化操作")
    }

    @Deprecated("Deprecated in Java")
    override fun onDestroy() {
        super.onDestroy()
        L.i("初始化完成")

    }
}