package com.example.aivoiceapplication


import android.content.Intent
import android.widget.TextView
import com.example.aivoiceapplication.databinding.ActivityMainBinding
import com.example.aivoiceapplication.service.VoiceService
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.helper.ARouterHelper
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var arrayList = ArrayList<String>()
    override fun getTitleText(): String {
        return "AI语音助手"
    }

    override fun initEvent() {
        getBinding().recyclerView.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)
        getBinding().recyclerView.adapter= CommonAdapter(arrayList,
            object : CommonAdapter.OnBindDataListener<String> {
                override fun onBindViewHolder(
                    model: String,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {

                    var textView = viewHolder.getView<TextView>(R.id.text)
                    textView.text=model
                    viewHolder.itemView.setOnClickListener {
                        when(model){
                            //<item>天气</item>
                            //        <item>星座</item>
                            //        <item>笑话</item>
                            //        <item>地图</item>
                            //        <item>应用管理</item>
                            //        <item>语音设置</item>
                            //        <item>系统设置</item>
                            //        <item>开发者模式</item>
                            "天气"->ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
                            "星座"->ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
                            "笑话"->ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
                            "地图"->ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
                            "应用管理"->ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
                            "语音设置"->ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
                            "系统设置"->ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
                            "开发者模式"->ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
                        }
                    }

                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.item_main_list
                }

            })
    }

    override fun initData() {
        val arrayMainTitles = resources.getStringArray(com.example.lib_base.R.array.MainTitleArray)

        arrayMainTitles.forEach {
            arrayList.add(it)
        }
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
        startService(Intent(this, VoiceService::class.java))
//        getBinding().btn.setOnClickListener {
//            ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
//        }
        AndPermission.with(this)
            .runtime()
            .permission(Permission.RECORD_AUDIO)
            .onGranted(Action<List<String?>> { ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER) })
            .start()



    }

}