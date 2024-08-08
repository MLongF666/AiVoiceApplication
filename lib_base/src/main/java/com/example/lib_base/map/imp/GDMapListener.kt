package com.example.lib_base.map.imp

import com.amap.api.location.AMapLocation

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/8/8 15:13
 * @version: 1.0
 */
interface GDMapListener {
    fun onLocationChanged(var1: AMapLocation?)
}