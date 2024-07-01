package com.example.module_developer

import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.L
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTs
import com.example.module_developer.data.DeveloperListData
import com.example.module_developer.databinding.ActivityDeveLoperBinding

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/6/22 19:10
 * @version: 1.0
 */
@Route(path = ARouterHelper.PATH_DEVELOPER)
class DeveLoperActivity : BaseActivity<ActivityDeveLoperBinding>() {
    //标题
    private val mTypeTitle=0
    //内容
    private val mTypeContent=1

    private val mList=ArrayList<DeveloperListData>()

    override fun getTitleText(): String {
        return "开发者模式"
    }

    override fun initEvent() {

    }

    override fun initData() {
        var array = resources.getStringArray(com.example.lib_base.R.array.DeveloperListArray)
        array.forEach {
            if (it.contains("[")){
                //标题
                addItemData(mTypeTitle,it.replace("[","").replace("]",""))
            }else{
                //内容
                addItemData(mTypeContent,it)
            }
        }

        initListView()

    }

    private fun initListView() {
        getBinding().rvList.layoutManager= LinearLayoutManager(this)
        //分割线
        getBinding().rvList.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        //适配器
        getBinding().rvList.adapter=CommonAdapter(mList,object :CommonAdapter.OnMoreBindDataListener<DeveloperListData>{
            override fun onBindViewHolder(
                model: DeveloperListData,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                when(model.type){
                    mTypeTitle->{
                        //标题
                        var textView = viewHolder.getView<TextView>(R.id.tv_developer_title)
                        textView.text=model.text
                            //点击标题
                    }
                    mTypeContent->{
                        //内容
                        var textView = viewHolder.getView<TextView>(R.id.tv_developer_content)
                        textView.text="${position}.${model.text}"
                        viewHolder.itemView.setOnClickListener {
                            itemClick(position)
                        }
                    }
                }
            }

            override fun getItemViewType(position: Int): Int {
                return mList[position].type
            }

            override fun getLayoutId(type: Int): Int {
                return if (type==mTypeTitle){
                    R.layout.item_developer_title
                }else{
                    R.layout.item_developer_content
                }
            }

        })

    }

    override fun initViewBinding(): ActivityDeveLoperBinding {
        return ActivityDeveLoperBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }
    private fun addItemData(type:Int, text:String){
        mList.add(DeveloperListData(type,text))
    }
    //点击事件
    private fun itemClick(position:Int){
        when(position){
            1->ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)

            2->ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)

            3-> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)

            4->ARouterHelper.startActivity(ARouterHelper.PATH_MAP)

            5->ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)

            6->ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)

            7->ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)

            9->VoiceManager.startAsr()
            10->VoiceManager.stopAsr()
            11->VoiceManager.cancelAsr()
            12->VoiceManager.releaseAsr()


            14->VoiceManager.startWakeUp()
            15->VoiceManager.stopWakeUp()

            20->VoiceManager.ttsStart("你好，欢迎使用语音助手")
            21->VoiceManager.ttsPause()
            22->VoiceManager.ttsResume()
            23->VoiceManager.ttsStop()
            24->VoiceManager.ttsRelease()

        }
    }

}