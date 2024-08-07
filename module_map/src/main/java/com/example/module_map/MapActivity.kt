package com.example.module_map


import android.Manifest
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.map.MapManager
import com.example.module_map.databinding.ActivityMapBinding
import com.yanzhenjie.permission.Action

@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity<ActivityMapBinding>() {
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

    override fun initView() {
        MapManager.bindMapView(getBinding().mMapView)
        //判断权限
        if (checkPermission(permissions)){
            MapManager.setMyLocationEnabled(true)
        }else{
            requestPermission(permissions, Action<List<String>> {

            })


        }
    }

    override fun onResume() {
        super.onResume()
        MapManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        MapManager.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        MapManager.onDestroy()
    }

}