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
    fun home()
    fun setVolumeUp()
    fun setVolumeDown()
    fun quit()
    //按照名字拨打电话
    fun callPhoneForName(name: String)
    //按照号码拨打电话
    fun callPhoneForNumber(phone: String)
    //播放笑话
    fun playJoke()
    //笑话列表
    fun jokeList()
    fun conTellTime(word: String)
    fun conTellInfo(word: String)
    //规划-导航
    fun routeMap(word: String)
    //周边搜索
    fun nearByMap(word: String)
    fun aiRobot(string: String)

}