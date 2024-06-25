package com.example.module_weather

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lib_base.base.BaseActivity
import com.example.module_weather.databinding.ActivityWeatherBinding

class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    override fun getTitleText(): String {
        return "天气"
    }

    override fun initEvent() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
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