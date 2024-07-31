package com.example.lib_network

import com.example.lib_network.bean.JokeDataBean
import com.example.lib_network.bean.WeatherDataBean
import com.example.lib_network.http.HttpInterceptor
import com.example.lib_network.http.HttpKey
import com.example.lib_network.http.HttpUrl
import com.example.lib_network.impl.HttpImpService
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @description: TODO 对外的网络管理类
 * @author:  mlf
 * @date:   2021/3/26 10:02
 */
object HttpManager {
    private val pageSize = 10 //每页数据数量
    //客户端
    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpInterceptor()).build()
    }


    //天气对象
    private val retrofitWeather by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.WEATHER_BASE_URL) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
    }
    //========================天气========================
    //天气接口
    private val apiWeather by lazy {
        retrofitWeather.create(HttpImpService::class.java)
    }
    //获取天气接口
    fun queryWeather(city: String,callback: Callback<WeatherDataBean>) =
        apiWeather.getWeather(city,HttpKey.JUHE_KEY).enqueue(callback)
    //============================笑话=========================

    private val retrofitJoke by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.JOKE_BASE_URL) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
    }
    //笑话接口
    private val apiJoke by lazy {
        retrofitJoke.create(HttpImpService::class.java)
    }
    //获取单个笑话接口
    fun getJokeList(callback: Callback<JokeDataBean>) =
        apiJoke.getJoke(HttpKey.TIANJU_KEY, pageSize).enqueue(callback)
    //获取单个笑话接口
    fun getJoke(callback: Callback<JokeDataBean>) =
        apiJoke.getJoke(HttpKey.TIANJU_KEY, 1).enqueue(callback)
}