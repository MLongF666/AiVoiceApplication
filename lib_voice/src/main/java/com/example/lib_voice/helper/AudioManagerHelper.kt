package com.example.lib_voice.helper

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.media.AudioManager
import android.util.Log


/**
 * @description: TODO 音频管理类
 * @author: mlf
 * @date: 2024/8/9 10:24
 * @version: 1.0
 */
object AudioManagerHelper : AudioManager.OnAudioFocusChangeListener{
    private var myAudioFocusChangeListener:OnMyAudioFocusChangeListener? = null
    private lateinit var audioManager:AudioManager
    fun initHelper(mContext: Context){
        //TODO 初始化音频管理类
        audioManager=mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC ,AudioManager.AUDIOFOCUS_GAIN)
    }
    fun setOnMyAudioFocusChangeListener(listener:OnMyAudioFocusChangeListener){
        myAudioFocusChangeListener=listener
    }

    override fun onAudioFocusChange(focusChange: Int) {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        myAudioFocusChangeListener?.setVolume(volume)
        Log.d("AudioManagerHelper","音频焦点改变：$focusChange")
    }
    interface OnMyAudioFocusChangeListener{
        fun setVolume(volume: Int)
        fun getStreamType():Int
    }
}