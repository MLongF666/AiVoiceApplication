package com.example.lib_voice.manager

import android.content.Context
import com.example.lib_voice.tts.VoiceTTs


/**
 * 语音管理类
 */
object VoiceManager {
    //语音key
    const val VOICE_APP_ID = "87114144"
    const val VOICE_APP_KEY = "dTGD2VCXjJak3dtvWeGOLYUc"
    const val VOICE_APP_SECRET = "u86JN3mX5QrOwoc1snYhLAOHz5C1Wz4R"
    fun initManager(mContext: Context) {
        // 初始化语音管理类
        VoiceTTs.initTTS(mContext)
    }
    //tts start
    //播放
    fun start(text:String){
        VoiceTTs.start(text,null)
    }
    //播放且有回调
    fun start(text:String,mListener:VoiceTTs.OnTTSResultListener){

        VoiceTTs.start(text,mListener)
    }
    //暂停
    fun pause(){
        VoiceTTs.pause()
    }
    //恢复
    fun resume(){
       VoiceTTs.resume()
    }
    //停止
    fun stop(){
        VoiceTTs.stop()
    }
    //释放
    fun release(){
        VoiceTTs.release()
    }
    //设置发音人
    fun setPeople(people:Int){
        VoiceTTs.setPeople(people)
    }
    //设置语速
    fun setSpeed(speed:Int){
        VoiceTTs.setSpeed(speed)
    }
    //设置音量
    fun setVolume(volume:Int){
        VoiceTTs.setVolume(volume)
    }



    //tts end
}