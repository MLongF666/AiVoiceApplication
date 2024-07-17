package com.example.lib_base.helper.`fun`

import android.app.Instrumentation
import android.content.Context
import android.util.Log
import android.view.KeyEvent

/**
 * @description: TODO 通用设置帮助类
 * @author: mlf
 * @date: 2024/7/17 10:09
 * @version: 1.0
 */
object CommonSettingHelper {
    private lateinit var mContext: Context
    private lateinit var mInstrumentation:Instrumentation
    fun initHelper(mContext: Context) {
        this.mContext = mContext
        mInstrumentation = Instrumentation()
    }

    //返回
    fun back(){
       Thread {
           Log.d("CommonSettingHelper", "back")
           mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
       }.start()

    }
    //主页
    fun home(){
         }
    }