package com.example.aivoiceapplication


import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aivoiceapplication.databinding.ActivityMainBinding
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.helper.ARouterHelper


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getTitleText(): String {
        return "AI语音助手"
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
        getBinding().btn.setOnClickListener {
            ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
        }
        getBinding().recyclerView.layoutManager=LinearLayoutManager(this)
        getBinding().recyclerView.adapter=CommonAdapter<String>(listOf("语音设置","天气","地图"," 笑话","星座","开发者")
            ,object :CommonAdapter.OnBindDataListener<String>{
                override fun onBindViewHolder(
                    model: String,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    var view = viewHolder.getView<TextView>(R.id.text)
                    view.text=model
//                    view.setOnClickListener {
//                        when(view.text){
//                            "语音设置"->{
//                                Toast.makeText(this@MainActivity,"语音设置",Toast.LENGTH_SHORT).show()
//                                ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
//                            }
//                            "天气"->{
//                                ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
//                            }
//                            "地图"->{
//                                ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
//                            }
//                            "笑话"->{
//                                Toast.makeText(this@MainActivity,"笑话",Toast.LENGTH_SHORT).show()
//                                ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
//                            }
//                        }
//                    }
                }
                override fun getLayoutId(type: Int): Int {
                    return R.layout.item_main_list
                }


        })

    }

}