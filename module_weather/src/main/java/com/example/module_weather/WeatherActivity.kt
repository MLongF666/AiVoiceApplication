package com.example.module_weather

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.WeatherDataBean
import com.example.module_weather.databinding.ActivityWeatherBinding
import com.example.module_weather.tools.WeatherIconTools
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    override fun getTitleText(): String {
        return "天气"
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    override fun initViewBinding(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        intent.run {
            var city = getStringExtra("city")
            if (city==null) {
                city = "北京"
            }else{
                loadWeather(city)
            }

        }
    }

    private fun loadWeather(city: String) {
        HttpManager.queryWeather(city, object : Callback<WeatherDataBean>{
            override fun onResponse(p0: Call<WeatherDataBean>, p1: Response<WeatherDataBean>) {
                p1.isSuccessful.run {
                    p1.body()?.let {
                        it.result.realtime.apply {
                            //设置天气
                            getBinding().mInfo.text=info
                            //设置图片
                            getBinding().mIvWid.setImageResource(WeatherIconTools.getWeatherIcon(wid))
                        }
                    }
                }

            }

            override fun onFailure(p0: Call<WeatherDataBean>, p1: Throwable) {

            }

        })
    }

}