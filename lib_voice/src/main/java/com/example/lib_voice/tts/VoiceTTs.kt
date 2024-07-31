package com.example.lib_voice.tts

import android.content.Context
import android.util.Log
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.example.lib_voice.manager.VoiceManager

/**
 * @description: TODO 百度AI TTS封装
 * @author: mlf
 * @date: 2024/6/25 10:19
 * @version: 1.0
 */
object VoiceTTs : SpeechSynthesizerListener {
    private var TAG=VoiceTTs::class.java.simpleName
    private lateinit var mSpeechSynthesizer: SpeechSynthesizer
    private var mListener:OnTTSResultListener?=null
    //初始化tts
    fun initTTS(mContext: Context){
        //初始化对象
        mSpeechSynthesizer = SpeechSynthesizer.getInstance()
        //设置上下文
        mSpeechSynthesizer.setContext(mContext)
        //设置key
        mSpeechSynthesizer.setAppId(VoiceManager.VOICE_APP_ID)
        mSpeechSynthesizer.setApiKey(VoiceManager.VOICE_APP_KEY,
            VoiceManager.VOICE_APP_SECRET)
        //设置监听
        mSpeechSynthesizer.setSpeechSynthesizerListener(this)
        //初始化
        mSpeechSynthesizer.initTts(TtsMode.ONLINE)
        //        //发声人
//        setPeople(5118)
//        //语速
//        setSpeed(6)
//        //音量
//        setVolume(15)
    }

    override fun onSynthesizeStart(p0: String?) {
        Log.d(TAG, "onSynthesizeStart: $p0 合成开始")
    }

    override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int, p3: Int) {

    }

    override fun onSynthesizeFinish(p0: String?) {
        Log.d(TAG, "合成结束")
    }

    override fun onSpeechStart(p0: String?) {
        Log.d(TAG, "开始播放：${p0.toString()}")
    }

    override fun onSpeechProgressChanged(p0: String?, p1: Int) {
        Log.d(TAG, "onSpeechProgressChanged: $p0, $p1")
    }

    override fun onSpeechFinish(p0: String?) {
        if (mListener!=null){
            mListener?.onTTEnd()
        }
        Log.d(TAG, "onSpeechFinish: 播放结束")
    }

    override fun onError(p0: String?, p1: SpeechError?) {
        Log.d(TAG, "onError: $p1 $p0")
    }
    //播放并且有回调
    fun start(text:String, mListener: OnTTSResultListener?){
        this.mListener=mListener
        Log.d(TAG, "onStart: $text")
        mSpeechSynthesizer.speak(text)
    }
    //暂停
    fun pause(){
        mSpeechSynthesizer.pause()
    }
    //恢复
    fun resume(){
        mSpeechSynthesizer.resume()
    }
    //停止
    fun stop(){
        mSpeechSynthesizer.stop()
    }
    //释放
    fun release(){
        mSpeechSynthesizer.release()
    }
    //接口
    interface OnTTSResultListener{
        //播放结束
        fun onTTEnd()
    }
    //设置发音人
    fun setPeople(people:Int){
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER,people.toString())
    }
    //设置语速
    fun setSpeed(speed:Int){
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED,speed.toString())
    }
    //设置音量
    fun setVolume(volume:Int){
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME,volume.toString())
    }

}