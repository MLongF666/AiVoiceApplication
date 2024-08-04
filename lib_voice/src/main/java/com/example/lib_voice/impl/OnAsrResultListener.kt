package com.example.lib_voice.impl

import org.json.JSONObject

/**
 * @description: TODO 语音识别接口
 * @author: mlf
 * @date: 2024/7/9 16:51
 * @version: 1.0
 */
interface OnAsrResultListener {
    //准备就绪
    fun weakUpReady()
    //开始说话
    fun asrStartSpeak()
    //结束说话
    fun asrStopSpeak()
    //唤醒成功
    fun weakUpSuccess(result: JSONObject)
    //唤醒失败
    fun weakUpError(text:String)
    //在线识别结果
    fun asrResult(result: JSONObject)
    //语义识别结果
    fun nluResult(result: JSONObject)

    fun updateUserText(text: String)
    //语音合成结果

}