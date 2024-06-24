package com.example.module_map

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lib_base.base.BaseActivity
import com.example.module_map.databinding.ActivityMapBinding

class MapActivity : BaseActivity<ActivityMapBinding>() {
    override fun getTitleText(): String {
    return "地图"
    }

    override fun initViewBinding(): ActivityMapBinding {
        return ActivityMapBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
    }

}