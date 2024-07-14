package com.example.module_app_manager


import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View

import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.BasePagerAdapter
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.`fun`.AppHelper
import com.example.lib_base.utils.L
import com.example.module_app_manager.databinding.ActivityAppManagerBinding


@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity<ActivityAppManagerBinding>() {
    private val waitApp=1000
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == waitApp) {
                waitAppHandler()
            }
        }
    }
    override fun getTitleText(): String {
        return "应用管理"
    }

    override fun initEvent() {

    }
    private fun waitAppHandler(){
        L.i("等待App列表刷新")
        if (AppHelper.mAllViewList.size>0){
            initViewPager()
        }else{
            //等待
            mHandler.sendEmptyMessageDelayed(waitApp,1000)
        }
    }
    override fun initData() {
        getBinding().loadingLayout.visibility= View.VISIBLE
        waitAppHandler()
    }

    private fun initViewPager() {
        getBinding().viewPagerAppManager.offscreenPageLimit=AppHelper.mAllViewList.size
        getBinding().viewPagerAppManager.adapter= BasePagerAdapter(AppHelper.mAllViewList)
        getBinding().loadingLayout.visibility= View.GONE
        getBinding().pointLayoutView.setPointCount(AppHelper.getPageSize())
        getBinding().viewPagerAppManager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                getBinding().pointLayoutView.setSelect(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun initViewBinding(): ActivityAppManagerBinding {
        return ActivityAppManagerBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }


    override fun initView() {

    }
}