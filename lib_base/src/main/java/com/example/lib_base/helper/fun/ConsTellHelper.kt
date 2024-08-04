package com.example.lib_base.helper.`fun`

import android.content.Context
import com.example.lib_base.R
import com.example.lib_base.utils.L

/**
 * @description: TODO 星座帮助类
 * @author: mlf
 * @date: 2024/8/1 10:25
 * @version: 1.0
 */
object ConsTellHelper {
    private lateinit var mConsTellArray: Array<String>
    private lateinit var mConsTellTimeArray: Array<String>
    private lateinit var mConsTellIdArray: IntArray
    fun initHelper(mContext: Context) {
        //初始化星座
        mConsTellArray = mContext.resources.getStringArray(R.array.ConstellArray)
        mConsTellTimeArray = mContext.resources.getStringArray(R.array.ConstellTimeArray)
        mConsTellIdArray = mContext.resources.getIntArray(R.array.ConsTellIdArray)
        L.d("initHelper: ${mConsTellArray.size}")
    }

    fun getConsTellTime(consTellName: String): String {
        mConsTellArray.forEachIndexed { index, s ->
            if (s == consTellName) {
                return mConsTellTimeArray[index]
            }
        }
        return "查询不到时间"
    }
    fun getConsTellId(consTellName: String): String {
        return when (consTellName) {
            "白羊座" -> "0"
            "金牛座" -> "1"
            "双子座" -> "2"
            "巨蟹座" -> "3"
            "狮子座" -> "4"
            "处女座" -> "5"
            "天秤座" -> "6"
            "天蝎座" -> "7"
            "射手座" -> "8"
            "摩羯座" -> "9"
            "水瓶座" -> "10"
            "双鱼座" -> "11"
            else -> "0"
        }
    }
}