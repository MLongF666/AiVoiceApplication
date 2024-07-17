package com.example.lib_network.bean

/**
 * @description: TODO 天气返回数据
 * @author: mlf
 * @date: 2024/7/17 16:47
 * @version: 1.0
 */
data class WeatherDataBean(
    val error_code: Int,
    val reason: String,
    val result: Result
)

data class Result(
    val city: String,
    val future: List<Future>,
    val realtime: Realtime
)

data class Future(
    val date: String,
    val direct: String,
    val temperature: String,
    val weather: String,
    val wid: Wid
)

data class Realtime(
    val aqi: String,
    val direct: String,
    val humidity: String,
    val info: String,
    val power: String,
    val temperature: String,
    val wid: String
)

data class Wid(
    val day: String,
    val night: String
)