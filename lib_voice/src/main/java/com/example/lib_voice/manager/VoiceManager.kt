package com.example.lib_voice.manager

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.example.lib_voice.asr.VoiceAsr
import com.example.lib_voice.tts.VoiceTTs
import com.example.lib_voice.wakeup.VoiceWakeUp


/**
 * 语音管理类
 */
object VoiceManager :EventListener{
    private  var  TAG=VoiceManager::class.java.simpleName
    //语音key
    const val VOICE_APP_ID = "87114144"
    const val VOICE_APP_KEY = "dTGD2VCXjJak3dtvWeGOLYUc"
    const val VOICE_APP_SECRET = "u86JN3mX5QrOwoc1snYhLAOHz5C1Wz4R"
    fun initManager(mContext: Context) {
        // 初始化语音管理类
        VoiceTTs.initTTS(mContext)
        VoiceWakeUp.initWakeUp(mContext,this)
        VoiceAsr.initAsr(mContext,this)
    }
    //tts start
    //播放
    fun ttsStart(text:String){
        Log.d(TAG, "ttsStart: $text")
        VoiceTTs.start(text,null)
    }
    //播放且有回调
    fun ttsStart(text:String,mListener:VoiceTTs.OnTTSResultListener){
        Log.d(TAG, "ttsStart--: $text")
        VoiceTTs.start(text,mListener)
    }
    //暂停
    fun ttsPause(){
        VoiceTTs.pause()
    }
    //恢复
    fun ttsResume(){
       VoiceTTs.resume()
    }
    //停止
    fun ttsStop(){
        VoiceTTs.stop()
    }
    //释放
    fun ttsRelease(){
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

    //启动唤醒
    fun startWakeUp(){
        Log.d(TAG, "startWakeUp: ")
        VoiceWakeUp.startWakeUp()
    }
    //停止唤醒
    fun stopWakeUp(){
        Log.d(TAG, "stopWakeUp: ")
        VoiceWakeUp.stopWakeUp()
    }
    //-------Asr start-------
    fun startAsr(){
        VoiceAsr.startAsr()
    }
    fun stopAsr(){
        VoiceAsr.stopAsr()
    }
    fun cancelAsr(){
        VoiceAsr.cancelAsr()
    }
    fun releaseAsr(){
        VoiceAsr.releaseAsr(this)
    }



    //tts end
    override fun onEvent(name: String?, params: String?, byte: ByteArray?,
                         offset: Int, length: Int) {
        Log.d(TAG, "onEvent: $name $params $byte $offset $length")
        //语音前置状态
            when(name){
                SpeechConstant.CALLBACK_EVENT_WAKEUP_READY->Log.d(TAG, "唤醒准备就绪")
                SpeechConstant.CALLBACK_EVENT_ASR_BEGIN->Log.i(TAG, "开始说话")
                SpeechConstant.CALLBACK_EVENT_ASR_END->Log.i(TAG, "结束说话")
            }
            if (params==null){
                return
            }
            when(name){
                SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS-> ttsStart("我在")
                SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR->Log.e(TAG, "唤醒失败")
                SpeechConstant.CALLBACK_EVENT_ASR_READY->Log.d(TAG, "ASR识别准备就绪")
                SpeechConstant.CALLBACK_EVENT_ASR_FINISH->Log.i(TAG,"识别结束${params}")
                SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL->{
                    byte?.let {
                        try {
                            val json = String(it, offset, length)
                            Log.d(TAG, "ASR动作识别结果: $json")
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
    }
    //tts end
}