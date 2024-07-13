package com.example.module_app_manager


import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.BasePagerAdapter
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.`fun`.AppHelper
import com.example.module_app_manager.databinding.ActivityAppManagerBinding


@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity<ActivityAppManagerBinding>() {
    private val waitApp=100
    private val mHandler= @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message) {
            if (msg.what==waitApp){
                waitAppHandler()
            }
        }
    }
    private var ll_loading: LinearLayout?=null
    private var viewPager: ViewPager?=null
    private var mViews = ArrayList<View>()
    override fun getTitleText(): String {
        return "应用管理"
    }

    override fun initEvent() {

    }
    private fun waitAppHandler(){
        if (AppHelper.getAllViewList().size>0){
            initViewPager()
        }else{
            //等待
            mHandler.sendEmptyMessageDelayed(waitApp,1000)
        }
    }
    override fun initData() {
        ll_loading?.visibility= View.VISIBLE
        waitAppHandler()
    }

    private fun initViewPager() {
        mViews=AppHelper.getAllViewList()
        Log.e("AppManagerActivity","mViews size="+mViews.size)
        viewPager = getBinding().viewPagerAppManager
        viewPager!!.offscreenPageLimit=AppHelper.getPageSize()
        viewPager!!.adapter= BasePagerAdapter(mViews)
        ll_loading?.visibility= View.GONE

        getBinding().pointLayoutView.setPointCount(AppHelper.getPageSize())
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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