package com.example.module_weather

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.module_weather.databinding.ActivityWeatherBinding

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
    }

}