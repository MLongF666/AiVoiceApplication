package com.example.lib_base.map

import android.content.Context
import com.baidu.location.LocationClient
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.model.LatLng

/**
 * @description: TODO 地图管理类
 * @author: mlf
 * @date: 2024/8/6 18:20
 * @version: 1.0
 */
object MapManager {
    //最大缩放
    private const val MAX_ZOOM=17f
    private var mMapView: MapView? = null
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mLocationClient: LocationClient
    fun initMap(mContext: Context){
        //TODO 初始化地图
        SDKInitializer.setAgreePrivacy(mContext, true)
        SDKInitializer.initialize(mContext)
        mLocationClient = LocationClient(mContext)
    }

    fun bindMapView(mMapView: MapView){
        this.mMapView = mMapView
        mBaiduMap= mMapView.map
        //默认缩放
        zoomMap(MAX_ZOOM)
        //默认卫星地图
        setMapType(1)
        //默认打开路况
        setTrafficEnabled(true)
        //默认打开热力图
        setBaiduHeatMapEnabled(true)
        //默认开启定位
        setMyLocationEnabled(true)
    }
    //设置默认中心点
    fun setCenter(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng))
    }
    //设置实时路况开关
    fun setTrafficEnabled(enabled: Boolean) {
        mBaiduMap.isTrafficEnabled = enabled
    }
    //设置百度热力图开关
    fun setBaiduHeatMapEnabled(enabled: Boolean) {
        mBaiduMap.isBaiduHeatMapEnabled = enabled
    }
    //设置开启地图定位图层

    fun setMyLocationEnabled(enabled: Boolean) {
        mBaiduMap.isMyLocationEnabled = enabled
    }

    fun setMapType(index:Int){
        mBaiduMap.mapType = if (index==0) BaiduMap.MAP_TYPE_NORMAL else BaiduMap.MAP_TYPE_SATELLITE
    }
    fun zoomMap(maxZoom: Float) {
        val builder = MapStatus.Builder()
        builder.zoom(maxZoom)
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
    }


    //===========生命周期==============
    fun onResume() {
        mMapView?.onResume()
    }
    fun onPause() {
        mMapView?.onPause()
    }
    fun onDestroy() {
        mMapView?.onDestroy()
        mMapView = null
    }


}