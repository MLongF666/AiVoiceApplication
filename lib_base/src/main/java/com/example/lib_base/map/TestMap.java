package com.example.lib_base.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/8/8 12:00
 * @version: 1.0
 */
public class TestMap {
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

        }
    };
}
