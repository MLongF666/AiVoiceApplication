package com.example.module_setting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.entity.AppConstants
import com.example.lib_base.event.EventManger
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.SpUtil
import com.example.module_setting.databinding.ActivitySettingBinding


@Route(path = ARouterHelper.PATH_SETTING)
class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getTitleText(): String {
        return "系统设置"
    }

    override fun initEvent() {
        getBinding().switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                SpUtil.put(AppConstants.IS_OPEN_VOICE_SPEECH,true)
            }else{
                SpUtil.put(AppConstants.IS_OPEN_VOICE_SPEECH,false)
            }
        }
        getBinding().switch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                SpUtil.put(AppConstants.IS_JOKE_BACKEND_PLAY,true)
            }else{
                SpUtil.put(AppConstants.IS_JOKE_BACKEND_PLAY,false)
            }
        }
    }

    override fun initData() {

    }

    override fun initViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        SpUtil.get(AppConstants.IS_OPEN_VOICE_SPEECH,false).apply {
            getBinding().switch1.isChecked = this as Boolean
        }
        SpUtil.get(AppConstants.IS_JOKE_BACKEND_PLAY,false).apply {
            getBinding().switch2.isChecked = this as Boolean
        }
    }

}