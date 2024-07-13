package com.example.lib_base.helper

import android.content.Context
import android.media.SoundPool

/***
 * 播放铃声
 */

object SoundPoolHelper {
    private lateinit var mContext: Context
    private lateinit var mSoundPool: SoundPool
    fun init(mContext: Context){
        this.mContext = mContext
        mSoundPool=SoundPool.Builder().setMaxStreams(1).build()
    }
    //播放铃声
     fun play(resId: Int){
         val poolId = mSoundPool.load(mContext, resId, 1)
         mSoundPool.setOnLoadCompleteListener { _, _, status ->
             if (status == 0) {
                 mSoundPool.play(poolId, 1f, 1f, 1, 0, 1f)
             }
         }

     }
}