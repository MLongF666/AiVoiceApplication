package com.example.aivoiceapplication


import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.aivoiceapplication.data.MainListData
import com.example.aivoiceapplication.databinding.ActivityMainBinding
import com.example.aivoiceapplication.service.VoiceService
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.base.impl.OnItemClick
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.trasformer.ScaleInTransformer
import com.example.lib_base.utils.L
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.WeatherDataBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity<ActivityMainBinding>() {
//    private val WINDOW_PERMISSION=1000

    private var arrayList = ArrayList<String>()
    private var mList = ArrayList<MainListData>()
    private var mViewList = ArrayList<View>()
    override fun getTitleText(): String {
        return "AI语音助手"
    }

    override fun initEvent() {
    }

    override fun initData() {
        val arrayMainTitles = resources.getStringArray(com.example.lib_base.R.array.MainTitleArray)

        arrayMainTitles.forEach {
            arrayList.add(it)
        }
        queryWater()
    }

    private fun queryWater() {
//        HttpManager.queryWeather("平顶山",object : Callback<WeatherDataBean> {
//            override fun onResponse(p0: Call<WeatherDataBean>, p1: Response<WeatherDataBean>) {
//
//            }
//
//            override fun onFailure(p0: Call<WeatherDataBean>, p1: Throwable) {
//
//            }
//
//        })

    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
        //动态权限
        if (checkPermission(Manifest.permission.RECORD_AUDIO)){
            linkService()
        }else{
            requestPermission(arrayOf(Manifest.permission.RECORD_AUDIO)
            ) { linkService() }
        }
        //窗口权限
        if (!checkWindowPermission()){
            requestWindowPermission(packageName)
        }
        initPageData()
        initPageView()
    }

    private fun initPageView() {
        var height = windowManager.defaultDisplay.height

        var commonAdapter = CommonAdapter<MainListData>(mList,
            object : CommonAdapter.OnBindDataListener<MainListData> {
                override fun onBindViewHolder(
                    model: MainListData,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    var cardView = viewHolder.getView<CardView>(R.id.bg)
                    cardView.setCardBackgroundColor(model.color)
                    cardView.layoutParams?.let { lp ->
                        lp.height = height / 5 * 3
                    }
                    var textView = viewHolder.getView<TextView>(R.id.text)
                    textView.text = model.title
                    var icon = viewHolder.getView<ImageView>(R.id.icon)
                    icon.setImageResource(model.icon)
                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.item_main_list
                }
            })
        getBinding().viewPager.adapter= commonAdapter
        getBinding().viewPager.offscreenPageLimit=mList.size
        var transformer = CompositePageTransformer()
        transformer.addTransformer(ScaleInTransformer())
        transformer.addTransformer(MarginPageTransformer(20))
        getBinding().viewPager.setPageTransformer(transformer)
        commonAdapter.setOnItemClick(object :OnItemClick<MainListData>{
            override fun onItemClick(position: Int, view: View, t: MainListData) {
                Toast.makeText(this@MainActivity,t.title, Toast.LENGTH_SHORT).show()
                when(t.title){
                    "应用管理"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
                    }
                    "开发者模式"-> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
                    }
                    "天气"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
                    }
                    "星座"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
                    }
                    "笑话"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
                    }
                    "地图"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
                    }
                    "语音设置"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
                    }
                    "系统设置"->{
                        ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
                    }
                    else->{
                        return
                    }
                }
            }
        })


    }


    private fun initPageData() {

        //获取页面数据
        var titleArray = resources.getStringArray(com.example.lib_base.R.array.MainTitleArray)
        var colorsArray = resources.getIntArray(R.array.MainColorArray)
        var iconsArray = resources.obtainTypedArray(R.array.MainIconArray)

        for ((index, value ) in titleArray.withIndex()){
            mList.add(MainListData(value,colorsArray[index],
                iconsArray.getResourceId(index,0)))
        }



    }

    private fun linkService() {
        startService(Intent(this, VoiceService::class.java))
    }

}