package com.example.lib_base.map.imp

import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.Marker
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.route.BusRouteResult
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.WalkRouteResult

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
    fun onPoiSearched(p0: PoiResultV2?, p1: Int)
    fun onPoiItemSearched(p0: PoiItemV2?, p1: Int)
    fun onMarkerClick(it: Marker?)
    fun onBusRouteSearched(p0: BusRouteResult?, p1: Int)
    fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int)
    fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int)
    fun onRideRouteSearched(p0: RideRouteResult?, p1: Int)
}