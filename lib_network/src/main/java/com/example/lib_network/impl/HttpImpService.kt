package com.example.lib_network.impl

import com.example.lib_network.bean.AiRootBean
import com.example.lib_network.bean.JokeDataBean
import com.example.lib_network.bean.WeatherDataBean
import com.example.lib_network.bean.constell.MonthData
import com.example.lib_network.bean.constell.TodayData
import com.example.lib_network.bean.constell.WeekData
import com.example.lib_network.bean.constell.YearData
import com.example.lib_network.http.HttpUrl
import retrofit2.Call
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
    fun getWeather(@Query("city") city: String,@Query("key")key: String):
            Call<WeatherDataBean>


    @GET(HttpUrl.JOKE_ACTION)
    fun getJoke(@Query("key")key: String,@Query("num")num: Number):
            Call<JokeDataBean>


    @GET(HttpUrl.CONSTELLATION_ACTION)
    fun getTodayConstellation(@Query("key")key: String,@Query("cid")cid: String,@Query("type")type: String):
            Call<TodayData>
    @GET(HttpUrl.CONSTELLATION_ACTION)
    fun getWeekConstellation(@Query("key")key: String,@Query("cid")cid: String,@Query("type")type: String):
            Call<WeekData>

    @GET(HttpUrl.CONSTELLATION_ACTION)
    fun getMonthConstellation(@Query("key")key: String,@Query("cid")cid: String,@Query("type")type: String):
            Call<MonthData>

    @GET(HttpUrl.CONSTELLATION_ACTION)
    fun getYearConstellation(@Query("key")key: String,@Query("cid")cid: String,@Query("type")type: String):
            Call<YearData>

    @GET(HttpUrl.AI_ACTION)
    fun getRobot(@Query("key")robotKey: String,@Query("keywords")msg: String): Call<AiRootBean>
}