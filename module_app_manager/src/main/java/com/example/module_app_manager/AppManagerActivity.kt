package com.example.module_app_manager


import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.SpUtil
import com.example.module_app_manager.databinding.ActivityAppManagerBinding

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity<ActivityAppManagerBinding>() {
    override fun getTitleText(): String {
        return "应用管理"
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    override fun initViewBinding(): ActivityAppManagerBinding {
        return ActivityAppManagerBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }


    override fun initView() {
        getBinding().btn2.setOnClickListener {
            Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show()
        }
//        getBinding().tv.text= SpUtil.get("name","").toString()?:""


    }
}