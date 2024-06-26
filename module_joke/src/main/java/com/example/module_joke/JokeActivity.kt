package com.example.module_joke

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.module_joke.databinding.ActivityJokeBinding

@Route(path = ARouterHelper.PATH_JOKE)
class JokeActivity : BaseActivity<ActivityJokeBinding>() {
    override fun getTitleText(): String {
        return "笑话"
    }

    override fun initEvent() {

    }

    override fun initData() {

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