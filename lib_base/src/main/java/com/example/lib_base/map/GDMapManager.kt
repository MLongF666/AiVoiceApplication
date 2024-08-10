package com.example.lib_base.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.apis.utils.core.api.AMapUtilCoreApi
import com.example.lib_base.map.imp.GDMapListener
import com.example.lib_base.utils.L


/**
 * @description: TODO 高德地图管理类
 * @author: mlf
 * @date: 2024/8/7 12:04
 * @version: 1.0
 */
@SuppressLint("StaticFieldLeak")
object GDMapManager :AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {
    private lateinit var mGeocodeSearch: GeocodeSearch
    //声明AMapLocationClient类对象
    private lateinit var mListener: GDMapListener
    private lateinit var mLocationClient: AMapLocationClient
    private lateinit var option :AMapLocationClientOption
    //声明定位回调监听器
    private lateinit var mMapView: MapView
    private lateinit var mMap: AMap
    fun initMapLocation(mContext: Context){
        //TODO 定位合规检查
        AMapLocationClient.updatePrivacyShow(mContext,true,true)
        AMapLocationClient.updatePrivacyAgree(mContext,true)
        option = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn)
         //高精度
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
         //低功耗 只使用网络定位
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        // 设置超时时间 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(10000);
         //关闭缓存机制
        option.setLocationCacheEnable(false)

        try {
            //初始化定位
            mLocationClient= AMapLocationClient(mContext)
            setCollectInfo(true)
        } catch (e: Exception) {
            L.e("error init location ${e.message}")
        }
        mLocationClient.setLocationOption(option)
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        setSwitchLocation(true)
        setMyLocationEnabled(true)
        mGeocodeSearch = GeocodeSearch(mContext)
        mGeocodeSearch.setOnGeocodeSearchListener(this)


    }
    fun setSwitchLocation(isSwitch: Boolean){
        if (!isSwitch){
            mLocationClient.stopLocation()
        }else{
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
            mLocationClient.setLocationListener(this)
        }
    }
    fun setMapType(type: Int){
        mMap.mapType = type
    }
    //设置显示当前位置
    fun setMyLocationEnabled(isEnabled: Boolean){
        mMap.isMyLocationEnabled = isEnabled
    }
    fun bindMapView(mMapView: MapView, bundle: Bundle?,mContext: Context){
        this.mMapView = mMapView
        L.i("mMap init success")
        this.mMapView.onCreate(bundle)
        mMap = this.mMapView.map
        initMapLocation(mContext)
    }
    fun getAMap(): AMap{
        return mMap
    }
    //设置是否允许采集个人设备信息
    fun setCollectInfo(enable: Boolean){
        AMapUtilCoreApi.setCollectInfoEnable(enable);
    }
    //设置是否打开地形图
    fun setTerrainEnable(enable: Boolean){
        MapsInitializer.setTerrainEnable(enable)
    }
    fun onResume(){
        mMapView.onResume()
    }
    fun onPause(){
        mMapView.onPause()
    }
    fun onDestroy(){
        mMapView.onDestroy()
    }

    fun setGDMapListener(listener: GDMapListener){
        this.mListener = listener
    }
    override fun onLocationChanged(p0: AMapLocation?) {
        L.i("onLocationChanged ${p0?.latitude}")
        p0?.let {
            val latLonPoint = LatLonPoint(p0.latitude, p0.longitude)
            val query = RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP)
            mGeocodeSearch.getFromLocationAsyn(query);
        }
        mListener.onLocationChanged(p0)
    }

    override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
        mListener.onRegeocodeSearched(p0,p1)
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
        mListener.onGeocodeSearched(p0,p1)
    }
}