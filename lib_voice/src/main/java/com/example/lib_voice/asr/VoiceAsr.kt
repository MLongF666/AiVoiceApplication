package com.example.lib_voice.asr

import android.content.Context
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.example.lib_voice.manager.VoiceManager
import org.json.JSONObject

/**
 * @description: TODO 语音识别
 * @author: mlf
 * @date: 2024/6/28 20:51
 * @param:
 * @return:
 * @version: 1.0
 */
object VoiceAsr {
    private lateinit var asr: EventManager
    private lateinit var asrJson: String
    fun initAsr(mContext: Context,listener: EventListener){
        val map = HashMap<Any, Any>()
//        "assets:///asr.bin".also { map[SpeechConstant.ASR] = it }
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME]=true
        map[SpeechConstant.ACCEPT_AUDIO_DATA]=false
        map[SpeechConstant.DISABLE_PUNCTUATION]=false
        map[SpeechConstant.PID]=15363//15373
        //设置key
        map[SpeechConstant.APP_ID]= VoiceManager.VOICE_APP_ID
        map[SpeechConstant.APP_KEY]= VoiceManager.VOICE_APP_KEY
        map[SpeechConstant.SECRET]= VoiceManager.VOICE_APP_SECRET
        //转换成Json
        asrJson= JSONObject(map as Map<Any, Any>?).toString()
        asr = EventManagerFactory.create(mContext, "asr")
        asr.registerListener(listener)
    }
    fun startAsr(){
        asr.send(SpeechConstant.ASR_START,asrJson,null,0,0)
    }
    fun stopAsr(){
        asr.send(SpeechConstant.ASR_STOP,null,null,0,0)
    }

    //取消识别
    fun cancelAsr(){
        asr.send(SpeechConstant.ASR_CANCEL,null,null,0,0)
    }
    //销毁识别
    fun releaseAsr(listener: EventListener){
        asr.send(SpeechConstant.ASR_CANCEL,null,null,0,0)
        asr.unregisterListener(listener)
    }
}