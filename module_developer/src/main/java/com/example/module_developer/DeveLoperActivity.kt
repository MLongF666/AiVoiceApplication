package com.example.module_developer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lib_base.base.BaseActivity
import com.example.module_developer.databinding.ActivityDeveLoperBinding

class DeveLoperActivity : BaseActivity<ActivityDeveLoperBinding>() {
    override fun getTitleText(): String {
        return "开发者"
    }

    override fun initViewBinding(): ActivityDeveLoperBinding {
        return ActivityDeveLoperBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }

}