package com.example.lib_network.http

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @description: TODO 拦截器
 * @author: mlf
 * @date: 2024/7/10 16:44
 * @version: 1.0
 */
class HttpInterceptor : Interceptor {
    private val TAG = "HttpInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.i(TAG,"============REQUEST==============")
        if (request.method()=="GET") {
            Log.i(TAG,"GET请求地址：${request.url()}")
        }
        Log.i(TAG,"请求体：${request.body()}")
        Log.i(TAG,"============RESPONSE==============")
//        Log.i(TAG,"响应体：${response.body()?.string()}")

        return response
    }
}