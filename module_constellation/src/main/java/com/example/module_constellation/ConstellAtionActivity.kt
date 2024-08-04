package com.example.module_constellation
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.`fun`.ConsTellHelper
import com.example.module_constellation.adapter.FragmentAdapter
import com.example.module_constellation.databinding.ActivityConstellBinding
import com.example.module_constellation.fragments.MonthFragment
import com.example.module_constellation.fragments.TodayFragment
import com.example.module_constellation.fragments.TomorrowFragment
import com.example.module_constellation.fragments.WeekFragment
import com.example.module_constellation.fragments.YearFragment
import com.google.android.material.tabs.TabLayoutMediator


@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellAtionActivity : BaseActivity<ActivityConstellBinding>() {
    private lateinit var mFragments:ArrayList<Fragment>
    override fun getTitleText(): String {
        return "星座"
    }

    override fun initEvent() {

    }

    override fun initData() {
        val name = intent.getStringExtra("word")
        if (name != null) {
            //语音进来的
            initPage(name)
        }else{
            //主页进来的
            initPage("水平座")
        }
    }

    override fun initViewBinding(): ActivityConstellBinding {
        return ActivityConstellBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }
    private fun initPage(name:String){
        initFragments(name)
        val adapter = FragmentAdapter(lifecycle, supportFragmentManager, mFragments)
        val viewPager = getBinding().viewPager
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = mFragments.size
        val tabLayout = getBinding().tabLayout
        TabLayoutMediator(tabLayout, viewPager){
            tab, position ->
            when(position){
                0->{
                    tab.text = "今天"
                }
                1->{
                    tab.text = "明天"
                }
                2->{
                    tab.text = "本周"
                }
                3->{
                    tab.text = "本月"
                }
                4->{
                    tab.text = "本年"
                }
            }
        }.attach()
    }

    private fun initFragments(name:String) {
        mFragments = ArrayList()
        val cid = ConsTellHelper.getConsTellId(name)
        val todayFragment = TodayFragment.newInstance(cid)
        val tomorrowFragment = TomorrowFragment.newInstance(cid)
        val weekFragment = WeekFragment.newInstance(cid)
        val monthFragment = MonthFragment.newInstance(cid)
        val yearFragment = YearFragment.newInstance(cid)


        mFragments.add(todayFragment)
        mFragments.add(tomorrowFragment)
        mFragments.add(weekFragment)
        mFragments.add(monthFragment)
        mFragments.add(yearFragment)
    }

}