package com.example.module_map


import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.module_map.databinding.ActivityMapBinding

@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity<ActivityMapBinding>() {
    override fun getTitleText(): String {
    return "地图"
    }

    override fun initEvent() {

    }

    override fun initData() {

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