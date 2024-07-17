package com.example.lib_network.impl

import com.example.lib_network.bean.WeatherDataBean
import com.example.lib_network.http.HttpUrl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @description: TODO 接口服务
 * @author: mlf
 * @date: 2024/7/10 15:48
 * @version: 1.0
 */
interface HttpImpService {
    @GET(HttpUrl.WEATHER_ACTION)
    fun getWeather(@Query("city") city: String,@Query("key")key: String): Call<WeatherDataBean>
}