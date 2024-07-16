package com.example.lib_voice.impl

/**
 * @description: TODO 语义结果
 * @author: mlf
 * @date: 2024/7/9 22:04
 * @version: 1.0
 */
interface OnNluResultListener {
    //查询天气
    fun queryWeather(city: String, date: String)
    //听不懂你的话
    fun nluError()

}