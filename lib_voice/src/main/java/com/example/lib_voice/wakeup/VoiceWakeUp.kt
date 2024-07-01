package com.example.lib_voice.wakeup

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.example.lib_voice.manager.VoiceManager
import org.json.JSONObject

/**
 * @description: TODO 语音唤醒
 * @author: mlf
 * @date: 2024/6/25 21:41
 * @param:
 * @return:
 * @version: 1.0
 */
object VoiceWakeUp {
    //唤醒对象
    private lateinit var wp: EventManager
    //启动数据
    private lateinit var wakeUpJson: String
    fun initWakeUp(mContext: Context,listener:EventListener){
        val map = HashMap<Any, Any>()
        "assets:///WakeUp.bin".also { map[SpeechConstant.WP_WORDS_FILE] = it }
        //是否获取音量
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME]=false
        //设置key
        map[SpeechConstant.APP_ID]= VoiceManager.VOICE_APP_ID
        map[SpeechConstant.APP_KEY]= VoiceManager.VOICE_APP_KEY
        map[SpeechConstant.SECRET]= VoiceManager.VOICE_APP_SECRET
        //转换成Json
        wakeUpJson=JSONObject(map as Map<Any, Any>?).toString()
        //设置监听器
        wp = EventManagerFactory.create(mContext, "wp")
        wp.registerListener(listener)
        Log.d("VoiceWakeUp", "initWakeUp: $wakeUpJson")
    }
    //启动唤醒
    fun startWakeUp(){
        wp.send(SpeechConstant.WAKEUP_START, wakeUpJson, null, 0, 0)
    }
    //停止唤醒
    fun stopWakeUp(){
        wp.send(SpeechConstant.WAKEUP_STOP,null,null,0,0)
    }

}