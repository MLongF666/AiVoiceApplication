package com.example.lib_voice.engine

import android.util.Log
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.words.NluWords
import org.json.JSONObject

/**
 * @description: TODO 语音引擎分析
 * @author: mlf
 * @date: 2024/6/25 10:19
 * @version: 1.0
 */
object VoiceEngineAnalyze {
    private lateinit var mOnNluResultListener: OnNluResultListener
    //分析结构
    fun analyzeNlu(nlu: JSONObject, mOnNluResultListener: OnNluResultListener) {
        this.mOnNluResultListener = mOnNluResultListener
        //TODO 语音引擎分析
        var rawText = nlu.optString("raw_text")
        Log.d("VoiceEngineAnalyze", "rawText: $rawText")
        var results = nlu.optJSONArray("results")?: return
        var length = results.length()
        if ( length==0){
            return
        }else if (length==1){
            analyzeNluSingle(results[0] as JSONObject)
        }else if(length>1){
            //多条命中
        }

    }
    //处理单条结果
    private fun analyzeNluSingle(result: JSONObject) {
        var domain = result.optString("domain")
        var intent = result.optString("intent")
        var slots = result.optJSONArray("slots")?: return
        when (domain) {


        }

    }
}