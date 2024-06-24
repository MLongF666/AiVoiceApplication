package com.example.aivoiceapplication


import android.widget.Toast
import com.example.aivoiceapplication.databinding.ActivityMainBinding
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtil


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getTitleText(): String {
        return "AI语音助手"
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
        getBinding().btn1.setOnClickListener {
            Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show()
            L.d("点击了")
            SpUtil.put("name","张三")
            ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
        }
    }

}