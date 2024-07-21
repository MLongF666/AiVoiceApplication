package com.example.lib_base.helper.`fun`.data

import android.graphics.drawable.Drawable

/**
 * @description: TODO KT数据类
 * @author: mlf
 * @date: 2024/7/11 20:49
 * @version: 1.0
 */

//包名 应用名 icon 第一启动类 是否是系统应用
data class AppData(
    val packName: String,
    val appName: String,
    val appIcon: Drawable,
    val firstRunName: String,
    val isSystemApp: Boolean
)
//联系人
data class ContactData(
    val phoneName: String,
    val phoneNumber: String
)