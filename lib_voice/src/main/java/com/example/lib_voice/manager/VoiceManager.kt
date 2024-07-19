package com.example.lib_voice.manager

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.example.lib_voice.asr.VoiceAsr
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.tts.VoiceTTs
import com.example.lib_voice.wakeup.IFVoiceWakeUp
import com.example.lib_voice.wakeup.VoiceWakeUp
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.WakeuperListener
import com.iflytek.cloud.WakeuperResult
import org.json.JSONObject


/**
 * 语音管理类
 */
object VoiceManager :EventListener, WakeuperListener {
    private  var  TAG=VoiceManager::class.java.simpleName
    //接口
    private lateinit var mOnAsrResultListener:OnAsrResultListener
    //语音key
    const val VOICE_APP_ID = "94414114"
    const val VOICE_APP_KEY = "2RMkyrpU6GdRfD3OfIcCMcho"
    const val VOICE_APP_SECRET = "9S8qhfKTs8xX53gnCYpv6Ax9xuO4svfx"
    fun initManager(mContext: Context,ivmPath:String,mOnAsrResultListener: OnAsrResultListener) {
        this.mOnAsrResultListener=mOnAsrResultListener
        // 初始化语音管理类
        VoiceTTs.initTTS(mContext)
        VoiceAsr.initAsr(mContext,this)
        IFVoiceWakeUp.initWakeUp(mContext,ivmPath,this)
        VoiceWakeUp.initWakeUp(mContext,this)
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
        Log.d(TAG, "启动唤醒")
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
    //if stop
    fun ifStop(){
        IFVoiceWakeUp.stopWakeUp()
    }
    //if start
    fun ifStart(){
        IFVoiceWakeUp.startWakeUp(this)
    }
    //if end



    //tts end
    override fun onEvent(name: String?, params: String?, byte: ByteArray?,
                         offset: Int, length: Int)
    {
        Log.d(TAG, "onEvent: $name $params $byte $offset $length")
        //语音前置状态
        when (name) {
//            SpeechConstant.CALLBACK_EVENT_WAKEUP_READY -> mOnAsrResultListener.weakUpReady()
            SpeechConstant.CALLBACK_EVENT_ASR_BEGIN -> mOnAsrResultListener.asrStartSpeak()
            SpeechConstant.CALLBACK_EVENT_ASR_END -> mOnAsrResultListener.asrStopSpeak()
        }
        //去除脏数据
        if (params == null) {
            return
        }
        val allJson = JSONObject(params)
        Log.i("Test", "allJson:$name:$allJson")
        when (name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> mOnAsrResultListener.weakUpSuccess(
                allJson
            )
//            SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR -> mOnAsrResultListener.weakUpError("唤醒失败")
            SpeechConstant.CALLBACK_EVENT_ASR_FINISH -> mOnAsrResultListener.asrResult(allJson)
            SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL -> {
                mOnAsrResultListener.updateUserText(allJson.optString("best_result"))
                byte?.let {
                    val nlu = JSONObject(String(byte, offset, length))
                    mOnAsrResultListener.nluResult(nlu)
                }
            }
        }
    }

    override fun onBeginOfSpeech() {
        Log.d("IFVoiceWakeUp", "onBeginOfSpeech: ")
        mOnAsrResultListener.weakUpReady()
    }

    override fun onResult(p0: WakeuperResult?) {
        Log.d("IFVoiceWakeUp", "onResult: $p0")
//        mOnAsrResultListener.weakUpReady()
        // 唤醒成功，可以开始识别
        mOnAsrResultListener.weakUpSuccess(p0!!)

    }

    override fun onError(p0: SpeechError?) {
        mOnAsrResultListener.weakUpError("唤醒失败")
        Log.d("IFVoiceWakeUp", "onError: ")
    }

    override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
        Log.d("IFVoiceWakeUp", "IFonEvent: ")
    }

    override fun onVolumeChanged(p0: Int) {
//        Log.d("IFVoiceWakeUp", "onVolumeChanged: $p0")
    }
    //tts end
}