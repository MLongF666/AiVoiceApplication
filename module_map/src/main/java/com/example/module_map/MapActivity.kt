package com.example.module_map


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.content.res.TypedArray
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.route.BusRouteResult
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.WalkRouteResult
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.map.imp.GDMapListener
import com.example.lib_base.map.manager.GDMapManager
import com.example.lib_base.utils.L
import com.example.module_map.databinding.ActivityMapBinding
import com.yanzhenjie.permission.Action


@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity<ActivityMapBinding>() {
    private lateinit var mStartLatLonPoint: LatLonPoint
    private lateinit var mEndLatLonPoint: LatLonPoint
    private val POSITION_REQUEST_CODE: Int = 100
    private var mBundle:Bundle?=null
    private lateinit var mMapView: MapView
    private var markers: TypedArray? =null
    private val permissions = arrayOf(
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun getTitleText(): String {
        return "地图"
    }

    override fun initEvent() {

    }

    override fun initData() {
    }

    override fun initViewBinding(): ActivityMapBinding {
        return ActivityMapBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBundle=savedInstanceState
        mMapView = getBinding().map
        if (!isOpenLocation()){
            opeGsp()
        }else{
            initMap(mMapView, mBundle,application)
        }
    }
    @SuppressLint("Recycle")
    private fun initMap(mapView: MapView, mBundle: Bundle?, application: Application) {
        GDMapManager.bindMapView(mapView, mBundle,application)
        GDMapManager.setTerrainEnable(true)
        GDMapManager.setMapType(AMap.MAP_TYPE_NORMAL)
        //判断
        if(checkPermission(permissions)){
            //Option
            L.i("权限申请成功")
            setLocation()
        }else{
            requestPermission(permissions,Action<List<String>>{
                setLocation()
            })
        }
        markers = resources.obtainTypedArray(R.array.Markers)
    }
    private var mPositonLatLonPoint=LatLonPoint(0.0,0.0)
    private fun setLocation() {
        GDMapManager.setGDMapListener(object : GDMapListener {
            override fun onLocationChanged(var1: AMapLocation?) {
                L.i("onLocationChanged: MapActivity ${var1.toString()}")
                var1?.let {
                    when (it.errorCode) {
                        0 -> {
                            L.i(
                                "定位成功${it.locationType},${it.latitude}," +
                                        "${it.longitude},${it.city},${it.address}"
                            )
                            mPositonLatLonPoint = LatLonPoint(it.latitude, it.longitude)
                            mStartLatLonPoint = mPositonLatLonPoint
                            //定位成功之后开始POI搜索
//                            GDMapManager.poiSearchAround(
//                                "生活服务",
//                                "",
//                                latLonPoint,
//                                1,
//                                10,
//                                10000,
//                                this@MapActivity
//                            )
                            GDMapManager.setCameraPosition(LatLng(it.latitude, it.longitude), 15f)
                            GDMapManager.addMarkerDefault(
                                LatLng(it.latitude, it.longitude),
                                "当前位置",
                                ""
                            )
                        }

                        else -> {
                            L.i("定位:${it.toString()}")
                        }
                    }
                }
            }

            override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                p0?.regeocodeAddress?.let {

                    L.i(
                        "onRegeocodeSearched: MapActivity ${it.formatAddress},${it.country}" +
                                "${it.province},${it.city},${it.district}"
                    )
                }

            }

            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                L.i("onGeocodeSearched: ${p0?.geocodeQuery},${p1}")
            }

            override fun onPoiSearched(p0: PoiResultV2?, p1: Int) {
                if (p1 == 1000) {
                    L.i("onPoiSearched: ${p0?.pois?.size}")
                    //将 POI 显示在地图上
                    p0?.let {
                        //将查询到的 POI 以绘制点的方式显示在地图上
                        it.pois.forEachIndexed { index, poiItem ->
                            val markerId = markers?.getResourceId(index, 0)
                            GDMapManager.addMarker(
                                LatLng(poiItem.latLonPoint.latitude, poiItem.latLonPoint.longitude),
                                poiItem.title, poiItem.snippet, this@MapActivity, markerId!!
                            )
                        }
                    }
                }
            }

            override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
                L.i("onPoiItemSearched: ${p0?.toString()},${p1}")
            }

            override fun onMarkerClick(it: Marker?) {
                L.i("onMarkerClick: ${it?.title}")
                Toast.makeText(this@MapActivity, it?.title, Toast.LENGTH_SHORT).show()
                it?.let {
                    val position = it.position
                    mEndLatLonPoint = LatLonPoint(position.latitude, position.longitude)
                    if(mStartLatLonPoint.latitude!=0.0&&mEndLatLonPoint.latitude!=0.0){
                        GDMapManager.startWalkRouteSearch(
                            mStartLatLonPoint,
                            mEndLatLonPoint,
                            RouteSearch.WalkDefault
                        )
                    }
                    L.i("onMarkerClick: ${mStartLatLonPoint.toString()},${mEndLatLonPoint.toString()}")
                }
            }

            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

            }

            override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {

            }
            @SuppressLint("SuspiciousIndentation")
            override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
            }

            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        L.d("onActivityResult: MapActivity $requestCode,$resultCode")
        if (resultCode == RESULT_OK||resultCode==RESULT_CANCELED){
            if (requestCode==POSITION_REQUEST_CODE){
                initMap(getBinding().map, mBundle,application)
                GDMapManager.setSwitchLocation(true)
            }
        }
    }
    private fun opeGsp() {
        L.i("定位失败")
        AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("请打开定位")
            .setPositiveButton("确定") { dialog, which ->
                //跳转到gsp设置页面
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                startActivityForResult(intent, POSITION_REQUEST_CODE)
            }.setNegativeButton("取消"
            ) { _, _ -> finish() }
            .show()
    }

    override fun initView() {
        //获取应用包名
        L.i("包名:${application.packageName}")
    }
    //判断是否打开了定位
    private fun isOpenLocation(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    override fun onResume() {
        super.onResume()
        GDMapManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        GDMapManager.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
    GDMapManager.onDestroy()
    }

}