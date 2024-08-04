package com.example.lib_base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import com.example.lib_base.base.bean.City
import com.google.gson.Gson

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/8/3 16:05
 * @version: 1.0
 */
object AssetUtils {
    private lateinit var mContext: Context
    private lateinit var mAssets: AssetManager
    private lateinit var mGson: Gson
    private lateinit var json: City
     fun initUtils(mContext: Context) {
        this.mContext = mContext
        mAssets = mContext.assets
        mGson = Gson()
         json = getJson(loadAssetFile("city.json"), City::class.java)
    }


    fun getCity(): City{
        return json
    }

    private fun <T> getJson(loadAssetFile: String, java: Class<T>): T {
        return mGson.fromJson<T>(loadAssetFile, java)
    }

    private fun loadAssetFile(path: String):String{
        val inputStream = mAssets.open(path)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        inputStream.close()
        return String(byteArray,Charsets.UTF_8)
    }
}