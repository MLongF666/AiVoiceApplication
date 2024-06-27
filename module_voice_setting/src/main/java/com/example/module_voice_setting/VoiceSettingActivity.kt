package com.example.module_voice_setting

import android.widget.SeekBar
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
import com.example.module_setting.R
import com.example.module_setting.databinding.ActivityVoiceSettingBinding

/***
 * @description: TODO 语音设置页面
 * @author: mlf
 * @date: 2024/6/25 21:41
 * @param:
 * @return:
 * @version: 1.0
 */

@Route(path = ARouterHelper.PATH_VOICE_SETTING)
class VoiceSettingActivity : BaseActivity<ActivityVoiceSettingBinding>() {
    private var mList:ArrayList<String>?=null
    private var mPeopleListIndexs:ArrayList<Int>?=null
    override fun getTitleText(): String {
        return "语音设置"
    }

    override fun initEvent() {
        getBinding().btnTest.setOnClickListener {
            VoiceManager.start("你好啊，我是小明")
        }
        getBinding().voiceSettingSpeedSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getBinding().voiceSettingSpeedSeekbar.progress=progress
                VoiceManager.setSpeed(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        getBinding().voiceSettingVolumeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getBinding().voiceSettingVolumeSeekbar.progress=progress
                VoiceManager.setVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


    }

    override fun initData() {
        //初始化发音人列表
        initPeopleList()
    }

    private fun initPeopleList() {
        mList=ArrayList()
        mPeopleListIndexs=ArrayList()
        val peopleArray = resources.getStringArray(R.array.TTSPeople)
        val peopleIndexArray = resources.getIntArray(R.array.TTSPeopleIndex)
        peopleArray.forEach { mList!!.add(it) }
        peopleIndexArray.forEach {
            mPeopleListIndexs!!.add(it)
        }
        getBinding().voicePeoplesList.layoutManager= LinearLayoutManager(this)
        //设置分割线
        getBinding().voicePeoplesList.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        getBinding().voicePeoplesList.adapter=CommonAdapter(mList!!,object :CommonAdapter.OnBindDataListener<String>{
            override fun onBindViewHolder(
                model: String,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                var textView = viewHolder.getView<TextView>(R.id.tv_people_name)
                textView.text=model
                viewHolder.itemView.setOnClickListener {
                    L.i("点击了${model}")
                    L.i("点击了${mPeopleListIndexs!![position]}")
                    L.i("${position}")
                    VoiceManager.setPeople(mPeopleListIndexs!![position])
                }
            }

            override fun getLayoutId(type: Int): Int {
                return R.layout.item_voice_people_list
            }
        })
    }

    override fun initViewBinding(): ActivityVoiceSettingBinding {
        return ActivityVoiceSettingBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        //默认值
        getBinding().voiceSettingSpeedSeekbar.progress=5
        getBinding().voiceSettingVolumeSeekbar.progress=5
        //设置最大值
        getBinding().voiceSettingSpeedSeekbar.max=15
        getBinding().voiceSettingVolumeSeekbar.max=15



    }
}