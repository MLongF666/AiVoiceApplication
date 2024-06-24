package com.example.lib_base.utils

import android.util.Log
import com.example.lib_base.config.BuildConfig

/**
 * @description: TODO log 日志
 * @author: mlf
 * @date: 2024/6/24 16:32
 * @param:
 * @return:
 * @version: 1.0
 */
object L {
    private const val TAG:String="AIVoice App"
    fun d(msg:String?){
        if (BuildConfig.DEBUG){
            msg?.let {
                Log.d(TAG,msg)
            }
        }
    }
    fun e(msg:String?){
        if (BuildConfig.DEBUG){
            msg?.let {
                Log.e(TAG,msg)
            }
        }
    }
    fun i(msg:String?){
        if (BuildConfig.DEBUG){
            msg?.let {
                Log.i(TAG,msg)
            }
        }
    }
    fun w(msg:String?){
        if (BuildConfig.DEBUG){
            msg?.let {
                Log.w(TAG,msg)
            }
        }
    }
    fun v(msg:String?){
        if (BuildConfig.DEBUG){
            msg?.let {
                Log.v(TAG,msg)
            }
        }
    }
    fun wtf(msg:String?){
        if (BuildConfig.DEBUG){
            msg?.let {
                Log.wtf(TAG,msg)
            }
        }
    }

}