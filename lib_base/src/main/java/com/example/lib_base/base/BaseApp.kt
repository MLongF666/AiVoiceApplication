package com.example.lib_base.base

import android.app.Application
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.SpUtil

class BaseApp : Application() {
    private lateinit var mApplication: Application
    override fun onCreate() {
        super.onCreate()
        ARouterHelper.initHelper(this)
        SpUtil.init(this)
    }
     fun getApplication(): Application {
        return this.mApplication
    }
}