package com.example.lib_network

import com.example.lib_network.bean.AiRootBean
import com.example.lib_network.bean.JokeDataBean
import com.example.lib_network.bean.WeatherDataBean
import com.example.lib_network.bean.constell.MonthData
import com.example.lib_network.bean.constell.TodayData
import com.example.lib_network.bean.constell.WeekData
import com.example.lib_network.bean.constell.YearData
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
    fun queryWeather(city: String, callback: Callback<WeatherDataBean>) =
        apiWeather.getWeather(city, HttpKey.JUHE_KEY).enqueue(callback)
    //============================笑话=========================



//笑话接口
    private val retrofitJoke by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.JOKE_BASE_URL) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
    }


    private val apiJoke by lazy {
        retrofitJoke.create(HttpImpService::class.java)
    }

    //获取单个笑话接口
    fun getJokeList(callback: Callback<JokeDataBean>) =
        apiJoke.getJoke(HttpKey.TIANJU_KEY, pageSize).enqueue(callback)

    //获取单个笑话接口
    fun getJoke(callback: Callback<JokeDataBean>) =
        apiJoke.getJoke(HttpKey.TIANJU_KEY, 1).enqueue(callback)

    //星座接口
    private val retrofitConstellation by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.CONSTELLATION_BASE_URL) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
    }
    private val apiConstellation by lazy {
        retrofitConstellation.create(HttpImpService::class.java)
    }
    //查询今天的星座运势
    fun queryTodayConstellation(cid: String, callback: Callback<TodayData>) =
        apiConstellation.getTodayConstellation(HttpKey.CONSTELLATION_KEY,cid,"today").enqueue(callback)
    //查询明天的星座运势
    fun queryTomorrowConstellation(cid: String, callback: Callback<TodayData>) =
        apiConstellation.getTodayConstellation(HttpKey.CONSTELLATION_KEY,cid,"tomorrow").enqueue(callback)
    //查询本周的星座运势
    fun queryThisWeekConstellation(cid: String, callback: Callback<WeekData>) =
        apiConstellation.getWeekConstellation(HttpKey.CONSTELLATION_KEY,cid,"week").enqueue(callback)
    //查询本月的星座运势
    fun queryThisMonthConstellation(cid: String, callback: Callback<MonthData>) =
        apiConstellation.getMonthConstellation(HttpKey.CONSTELLATION_KEY,cid,"month").enqueue(callback)
    //查询本年的星座运势
    fun queryThisYearConstellation(cid: String, callback: Callback<YearData>) =
        apiConstellation.getYearConstellation(HttpKey.CONSTELLATION_KEY,cid,"year").enqueue(callback)
    //机器人接口
    private val retrofitRobot by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.ROBOT_BASE_URL) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
    }
    private val apiRobot by lazy {
        retrofitRobot.create(HttpImpService::class.java)
    }
    fun queryRobot(msg: String, callback: Callback<AiRootBean>) =
        apiRobot.getRobot(HttpKey.ROBOT_KEY, msg).enqueue(callback)
}