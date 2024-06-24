package com.example.module_app_manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_manager)
    }
}