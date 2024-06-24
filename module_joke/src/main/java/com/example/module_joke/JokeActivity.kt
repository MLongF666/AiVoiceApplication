package com.example.module_joke

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lib_base.base.BaseActivity
import com.example.module_joke.databinding.ActivityJokeBinding

class JokeActivity : BaseActivity<ActivityJokeBinding>() {
    override fun getTitleText(): String {
        return "笑话"
    }

    override fun initViewBinding(): ActivityJokeBinding {
        return ActivityJokeBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }
}