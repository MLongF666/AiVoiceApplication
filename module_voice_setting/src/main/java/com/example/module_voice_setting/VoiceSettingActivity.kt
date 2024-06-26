package com.example.module_voice_setting

import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_voice.manager.VoiceManager
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
    override fun getTitleText(): String {
        return "语音设置"
    }

    override fun initEvent() {
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