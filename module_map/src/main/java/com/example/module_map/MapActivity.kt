package com.example.module_map


import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.RegeocodeResult
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.map.GDMapManager
import com.example.lib_base.map.imp.GDMapListener
import com.example.lib_base.utils.L
import com.example.lib_voice.manager.VoiceManager
import com.example.module_map.databinding.ActivityMapBinding
import com.yanzhenjie.permission.Action


@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity<ActivityMapBinding>() {
    private val POSITION_REQUEST_CODE: Int = 100
    private var mBundle:Bundle?=null
    private lateinit var mMapView: MapView
    private val permissions = arrayOf(
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
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
    private fun initMap(mapView: MapView, mBundle: Bundle?, application: Application) {
        GDMapManager.bindMapView(mapView, mBundle,application)
        GDMapManager.setTerrainEnable(true)
        GDMapManager.setMapType(AMap.MAP_TYPE_SATELLITE)
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
    }

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
                        }

                        else -> {
                            L.i("定位:${it.toString()}")
                        }
                    }
                }
            }

            override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                val address = p0?.regeocodeAddress
                L.i(
                    "onRegeocodeSearched: MapActivity ${address?.formatAddress},${address?.country}" +
                            "${address?.province},${address?.city},${address?.district}"
                )
//                p0?.regeocodeAddress?.formatAddress?.let { VoiceManager.ttsStart(it) }
            }

            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        L.d("onActivityResult: MapActivity $requestCode,$resultCode")
        if (resultCode == RESULT_OK||resultCode==RESULT_CANCELED){
            if (requestCode==POSITION_REQUEST_CODE){
                initMap(getBinding().map, mBundle,application)
                GDMapManager.setSwitchLocation(true)
                GDMapManager.setMyLocationEnabled(true)
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
            ) { dialog, which -> finish() }
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
//        BDMapManager.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
//        BDMapManager.onPause()
        mMapView.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
//        BDMapManager.onDestroy()
        mMapView.onDestroy()
    }

}