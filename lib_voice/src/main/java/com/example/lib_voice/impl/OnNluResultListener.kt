package com.example.lib_voice.impl

/**
 * @description: TODO 语义结果
 * @author: mlf
 * @date: 2024/7/9 22:04
 * @version: 1.0
 */
interface OnNluResultListener {
    //查询天气
    fun queryWeather(city: String)
    //查询天气详情
    fun queryWeatherInfo(city: String)
    //听不懂你的话
    fun nluError()
    fun openApp(word: String)
    fun unInstallApp(word: String)
    fun otherApp(word: String)
    //通用设置
    //返回
    fun back()
    //主页
    fun home(): Any?
    fun setVolumeUp(): Any?
    fun setVolumeDown(): Any?
    fun quit(): Any?
    fun callPhoneForName(name: String)
    fun callPhoneForNumber(phone: String)
    fun playJoke(): Any?
    fun jokeList(): Any?
    fun conTellTime(word: String)
    fun conTellInfo(word: String)
    fun routeMap(word: String)
    fun nearByMap(word: String)
    fun aiRobot(string: String)

}