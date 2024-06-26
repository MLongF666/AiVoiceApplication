package com.example.module_constellation


import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.module_constellation.databinding.ActivityConstellBinding

@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellAtionActivity : BaseActivity<ActivityConstellBinding>() {
    override fun getTitleText(): String {
        return "星座"
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    override fun initViewBinding(): ActivityConstellBinding {
        return ActivityConstellBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        getBinding().tv.text="constell"
    }

}