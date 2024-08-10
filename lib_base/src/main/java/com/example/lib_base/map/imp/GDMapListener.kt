package com.example.lib_base.map.imp

import com.amap.api.location.AMapLocation
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.RegeocodeResult

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/8/8 15:13
 * @version: 1.0
 */
interface GDMapListener {
    fun onLocationChanged(var1: AMapLocation?)
    fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int)
    fun onGeocodeSearched(p0: GeocodeResult?, p1: Int)
}