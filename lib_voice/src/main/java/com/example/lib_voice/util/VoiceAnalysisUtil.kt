package com.example.lib_voice.util

import com.example.lib_voice.words.Keyword

/**
 * @description: TODO 解析帮助类
 * @author: mlf
 * @date: 2024/7/15 22:22
 * @version: 1.0
 */
object VoiceAnalysisUtil {
   fun analysisOpenApp(text:String):String{
       //去除标点符号
       var result=analysisPun(text)
       //截取打开后面的字符串
       result=result.substring(result.indexOf(Keyword.KEY_OPEN_APP)+Keyword.KEY_OPEN_APP.length)
           //判断是否为字母
       if (result.matches("[a-zA-Z]+".toRegex())){
               //小写转大写
               result=result.toUpperCase()
       }


       return result
   }
    //去除标点符号方法

    private fun analysisPun(text: String): String {
        //去除标点符号
        val regex: String = "[^a-zA-Z0-9\\u4e00-\\u9fa5]"
        val result = text.replace(regex.toRegex(), "").toLowerCase()
        return result
    }


    fun analysisDelete(text: String): String {
        var result = analysisPun(text)
        //截取卸载后面的字符串
        result = result.substring(text.indexOf(Keyword.KEY_DELETE) + Keyword.KEY_DELETE.length)
        //判断是否为字母
        if (result.matches("[a-zA-Z]+".toRegex())) {
            //小写转大写
            result = result.toUpperCase()
        }
        return result

    }

    fun analysisApp(text: String):String {

        var result = text.substring(2,text.length-1)
        result = analysisPun(result)
        //判断是否为字母
        if (result.matches("[a-zA-Z]+".toRegex())) {
            //小写转大写
            result = result.toUpperCase()
        }
        return result
    }
}