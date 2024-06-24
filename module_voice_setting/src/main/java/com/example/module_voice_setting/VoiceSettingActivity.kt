package com.example.module_voice_setting

import com.example.lib_base.base.BaseActivity
import com.example.module_setting.databinding.ActivityVoiceSettingBinding

class VoiceSettingActivity : BaseActivity<ActivityVoiceSettingBinding>() {
    override fun getTitleText(): String {
        return "语音设置"
    }

    override fun initViewBinding(): ActivityVoiceSettingBinding {
        return ActivityVoiceSettingBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
    }
}