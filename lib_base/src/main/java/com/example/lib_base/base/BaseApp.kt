package com.example.lib_base.base

import android.app.Application
import com.example.lib_base.helper.ARouterHelper

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ARouterHelper.initHelper(this)
    }
}