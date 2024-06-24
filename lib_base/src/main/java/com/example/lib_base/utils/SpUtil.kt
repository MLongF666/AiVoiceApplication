package com.example.lib_base.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.lib_base.base.BaseApp

object SpUtil {
    private const val SP_NAME = "config"
    private lateinit var sp: SharedPreferences
    //初始化
    fun init(mContext: Context) {
        sp=mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }
    //对象
    fun put(key: String, value: Any) {
        val editor = sp.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
        }
        editor.apply()
    }
    fun get(key: String, defValue: Any): Any {
        return when (defValue) {
            is String -> sp.getString(key, defValue)
            is Int -> sp.getInt(key, defValue)
            is Boolean -> sp.getBoolean(key, defValue)
            is Float -> sp.getFloat(key, defValue)
            is Long -> sp.getLong(key, defValue)
            else -> defValue
        }!!
    }
}