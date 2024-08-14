package com.example.lib_base.map.manager
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import com.amap.api.navi.AmapPageType
import com.amap.api.navi.INaviInfoCallback
import com.amap.api.navi.model.AMapCalcRouteResult
import com.amap.api.navi.model.AMapLaneInfo
import com.amap.api.navi.model.AMapModelCross
import com.amap.api.navi.model.AMapNaviCameraInfo
import com.amap.api.navi.model.AMapNaviCross
import com.amap.api.navi.model.AMapNaviLocation
import com.amap.api.navi.model.AMapNaviRouteNotifyData
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo
import com.amap.api.navi.model.AMapServiceAreaInfo
import com.amap.api.navi.model.AimLessModeCongestionInfo
import com.amap.api.navi.model.AimLessModeStat
import com.amap.api.navi.model.NaviInfo
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import com.amap.api.services.route.BusRouteResult
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.WalkRouteResult
import com.amap.apis.utils.core.api.AMapUtilCoreApi
import com.example.lib_base.map.imp.GDMapListener
import com.example.lib_base.map.overlay.WalkRouteOverlay
import com.example.lib_base.utils.L


/**
 * @description: TODO 高德地图管理类
 * @author: mlf
 * @date: 2024/8/7 12:04
 * @version: 1.0
 */
@SuppressLint("StaticFieldLeak")
object GDMapManager :AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, INaviInfoCallback,
    PoiSearchV2.OnPoiSearchListener, RouteSearch.OnRouteSearchListener, AMapNaviListener,
    AMap.InfoWindowAdapter {
    private lateinit var mGeocodeSearch: GeocodeSearch
    private lateinit var mContext:Context
    //声明AMapLocationClient类对象
    private lateinit var mListener: GDMapListener
    private lateinit var mLocationClient: AMapLocationClient
    private lateinit var option :AMapLocationClientOption
    private lateinit var mRouteSearch: RouteSearch
    //声明定位回调监听器
    private  var mMapView: MapView?=null
    private lateinit var mMap: AMap
    private var query:PoiSearchV2.Query?=null
    //绑定地图视图
    fun bindMapView(mMapView: MapView, bundle: Bundle?,mContext: Context){
        GDMapManager.mMapView = mMapView
        L.i("mMap init success")
        GDMapManager.mMapView!!.onCreate(bundle)
        mMap = GDMapManager.mMapView!!.map
        initMapLocation(mContext)
        this.mContext = mContext
        initRouteSearch(mContext)
    }
    //初始化定位
    private fun initMapLocation(mContext: Context){
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
            mLocationClient = AMapLocationClient(mContext)
            setCollectInfo(true)
        } catch (e: Exception) {
            L.e("error init location ${e.message}")
        }
        mLocationClient.setLocationOption(option)
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        setSwitchLocation(true)
        setMyLocationEnabled(false)
        mGeocodeSearch = GeocodeSearch(mContext)
        mGeocodeSearch.setOnGeocodeSearchListener(this)
    }
    //设置定位开关
    fun setSwitchLocation(isSwitch: Boolean){
        if (!isSwitch){
            mLocationClient.stopLocation()
        }else{
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
            mLocationClient.setLocationListener(this)
        }
    }
    //设置地图类型
    fun setMapType(type: Int){
        mMap.mapType = type
    }
    //设置显示当前位置
    private fun setMyLocationEnabled(isEnabled: Boolean){
        mMap.isMyLocationEnabled = isEnabled
    }
    //调整试图中心
    fun setCameraPosition(latLng: LatLng,zoom:Float){
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                latLng,
                zoom,
                0f,
                0f
            )
        )
        mMap.moveCamera(mCameraUpdate)
    }
    fun getAMap(): AMap{
        return mMap
    }
    //=======================================PIO搜索=============================================
    //关键字搜索
    fun poiSearchKeyWord(keyWord:String,poiType:String,city :String,currentPage:Int,mContext: Context){
        query = PoiSearchV2.Query(keyWord,poiType, city)
        query!!.pageSize = 10;// 设置每页最多返回多少条poiitem
        query!!.pageNum = currentPage;//设置查询页码
        val poiSearch = PoiSearchV2(mContext, query)
        poiSearch.setOnPoiSearchListener(this)
        poiSearch.searchPOIAsyn()
    }
    //范围搜索
    fun poiSearchAround(keyWord:String,poiType:String,latLonPoint: LatLonPoint,pageNum:Int,pageSize:Int,radius:Int,mContext: Context){
        query = PoiSearchV2.Query(keyWord,poiType)
        query!!.pageSize = pageSize;// 设置每页最多返回多少条poiitem
        query!!.pageNum = pageNum;//设置查询页码
        val poiSearch = PoiSearchV2(mContext, query)
        poiSearch.bound = PoiSearchV2.SearchBound(
            LatLonPoint(
                latLonPoint.latitude,
                latLonPoint.longitude
            ), radius
        ) //设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(this)
        poiSearch.searchPOIAsyn()
    }
    //=======================================导航=============================================
    private fun initRouteSearch(mContext: Context){
        mRouteSearch = RouteSearch(mContext)
        mRouteSearch.setRouteSearchListener(this)
    }
    //步行路线规划
    fun startWalkRouteSearch(mStartPoint: LatLonPoint, mEndPoint: LatLonPoint, mode: Int){
        val fromAndTo = RouteSearch.FromAndTo(
            mStartPoint, mEndPoint
        )
        //初始化query对象，fromAndTo是包含起终点信息，walkMode是步行路径规划的模式
        // 步行路径规划
        val query = RouteSearch.WalkRouteQuery(fromAndTo, mode)
        mRouteSearch.calculateWalkRouteAsyn(query) // 异步路径规划步行模式查询
    }
    //公交路线规划
    fun startBusRouteSearch(mStartPoint: LatLonPoint, mEndPoint: LatLonPoint, city: String, mode: Int, mCurrentCityName: String){
        val fromAndTo = RouteSearch.FromAndTo(
            mStartPoint, mEndPoint
        )
        //初始化query对象，fromAndTo是包含起终点信息，city是城市名称
        // 公交路径规划
        val query = RouteSearch.BusRouteQuery(
            fromAndTo, mode,
            mCurrentCityName, 0
        ) // 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
        mRouteSearch.calculateBusRouteAsyn(query) // 异步路径规划公交模式查询
    }
    //驾车路线规划
    fun startDriveRouteSearch(mStartPoint: LatLonPoint, mEndPoint: LatLonPoint, drivingMode: Int){
        val fromAndTo = RouteSearch.FromAndTo(
            mStartPoint, mEndPoint
        )
        val query = RouteSearch.DriveRouteQuery(
            fromAndTo, drivingMode, null, null, ""
        )
        // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
        // 第三个参数表示途经点（最多支持6个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);
        // 异步路径规划驾车模式查询
    }
    //骑行路线规划
    fun startRideRouteSearch(mStartPoint: LatLonPoint, mEndPoint: LatLonPoint, mode: Int){
        val fromAndTo = RouteSearch.FromAndTo(mStartPoint, mEndPoint)
        val query: RouteSearch.RideRouteQuery = RouteSearch.RideRouteQuery(fromAndTo, mode)
        mRouteSearch.calculateRideRouteAsyn(query)
    }
    //开启步行导航
    fun startWalkNavi(startPoint: LatLonPoint, endPoint: LatLonPoint,startName:String,endName:String){
        val startLatLng = LatLng(startPoint.latitude, startPoint.longitude)
        //起点
        val start: Poi = Poi(startName, startLatLng, "B000A28DAE")
        val endLatLng = LatLng(endPoint.latitude, endPoint.longitude)
      //途经点
//        val poiList: ArrayList<Poi> = ArrayList<Poi>()
//        poiList.add(Poi(endName, LatLng(39.918058, 116.397026), "B000A8UIN8"))
        //终点
        val end: Poi = Poi(endName, endLatLng, null)
        // 组件参数配置 //最后一个参数，AmapPageType.NAVI为导航界面，AmapPageType.ROUTE为路线规划界面
        val params = AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE)
        // 启动组件
        AmapNaviPage.getInstance().showRouteActivity(mContext.applicationContext, params, this)
        L.i("startWalkNavi: ${startPoint.latitude} ${startPoint.longitude} ${endLatLng.latitude} ${endLatLng.longitude}")
    }
    //设置是否允许采集个人设备信息
    private fun setCollectInfo(enable: Boolean){
        AMapUtilCoreApi.setCollectInfoEnable(enable);
    }
    //设置是否打开地形图
    fun setTerrainEnable(enable: Boolean){
        MapsInitializer.setTerrainEnable(enable)
    }
    fun addMarkerDefault(
        latLng: LatLng, tiitle: String,
        snippet: String?
    ) {
        val markerOption = MarkerOptions()
        markerOption.position(latLng)
        markerOption.title(tiitle).snippet(snippet)
        markerOption.draggable(false) //设置Marker可拖动
        markerOption.icon(
            BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true) //设置marker平贴地图效果
        // 定义 Marker 点击事件监听
        val markerClickListener = AMap.OnMarkerClickListener {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            mListener.onMarkerClick(it)
            false
        }
        mMap.addMarker(markerOption)
        // 绑定 Marker 被点击事件
        mMap.setOnMarkerClickListener(markerClickListener)
    }
    fun addMarker(
        latLng: LatLng, tiitle: String, snippet: String?,
        mContext: Context,
        icon: Int
    ) {
        val markerOption = MarkerOptions()
        markerOption.position(latLng)
        markerOption.title(tiitle).snippet(snippet)
        markerOption.draggable(false) //设置Marker可拖动
        val fromBitmap = BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(
                mContext.resources,
                icon
            )
        )
        markerOption.icon(
            fromBitmap
        )
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(false) //设置marker平贴地图效果

        // 定义 Marker 点击事件监听
        val markerClickListener = AMap.OnMarkerClickListener {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            mListener.onMarkerClick(it)
            false
        }
        mMap.addMarker(markerOption)
        // 绑定 Marker 被点击事件
        mMap.setOnMarkerClickListener(markerClickListener)
        mMap.setInfoWindowAdapter(this)
    }
//    private fun planWalkRoute(result: WalkRouteResult?, p1: Int) {
//        mMap.clear()
//        var mWalkRouteResult: WalkRouteResult? = null
//        if (p1== AMapException.CODE_AMAP_SUCCESS) {
//            if (result!=null&&result.paths!=null)
//                mWalkRouteResult=result
//            val walkPath = mWalkRouteResult?.paths?.get(0) ?: return
//            val walkRouteOverlay = WalkRouteOverlay(
//                mContext, mMap, walkPath,
//                mWalkRouteResult.startPos,
//                mWalkRouteResult.targetPos
//            )
//            walkRouteOverlay.removeFromMap()
//            walkRouteOverlay.addToMap()
//            walkRouteOverlay.zoomToSpan()
//
//        }
//    }
    fun setGDMapListener(listener: GDMapListener){
        mListener = listener
    }
    fun onResume(){
        mMapView?.onResume()
    }
    fun onPause(){
        mMapView?.onPause()
    }
    fun onDestroy(){
        mMapView?.onDestroy()
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
    override fun onPoiSearched(p0: PoiResultV2?, p1: Int) {
            mListener.onPoiSearched(p0,p1)
    }
    override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
        mListener.onPoiItemSearched(p0,p1)
    }
    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
        //BusRouteResult 公交换乘方案结果
        mListener.onBusRouteSearched(p0,p1)
    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
        //DriveRouteResult 驾车方案结果
        mListener.onDriveRouteSearched(p0,p1)
    }

    override fun onWalkRouteSearched(result: WalkRouteResult?, p1: Int) {
        //    WalkRouteResult 步行方案结果
        mListener.onWalkRouteSearched(result,p1)
        //规划路线方法
//        planWalkRoute(result,p1)
    }
    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
        //RideRouteResult 骑行方案结果
        mListener.onRideRouteSearched(p0,p1)
    }

    override fun onInitNaviFailure() {
        mListener.onInitNaviFailure()
    }

    override fun onInitNaviSuccess() {
        mListener.onInitNaviSuccess()
    }

    override fun onGetNavigationText(p0: String?) {
        mListener.onGetNavigationText(p0)
    }

    override fun onEndEmulatorNavi() {
    }

    override fun onArriveDestination() {

    }

    override fun onLocationChange(p0: AMapNaviLocation?) {

    }

    override fun onGetNavigationText(p0: Int, p1: String?) {

    }

    override fun onArriveDestination(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStartNavi(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onTrafficStatusUpdate() {
        TODO("Not yet implemented")
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        L.i("onCalculateRouteSuccess ${p0?.contentToString()}")
    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
        L.i("onCalculateRouteSuccess ${p0?.toString()}")
    }

    override fun notifyParallelRoad(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {
        TODO("Not yet implemented")
    }

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {
        TODO("Not yet implemented")
    }

    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {
        TODO("Not yet implemented")
    }

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {
        TODO("Not yet implemented")
    }

    override fun onPlayRing(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {
        TODO("Not yet implemented")
    }

    override fun onGpsSignalWeak(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onCalculateRouteFailure(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
        TODO("Not yet implemented")
    }

    override fun onReCalculateRouteForYaw() {
        TODO("Not yet implemented")
    }

    override fun onReCalculateRouteForTrafficJam() {
        TODO("Not yet implemented")
    }

    override fun onStopSpeaking() {
        TODO("Not yet implemented")
    }

    override fun onReCalculateRoute(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onExitPage(p0: Int) {
        if (p0 !=null){
            L.i("onExitPage $p0")
        }

    }

    override fun onStrategyChanged(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onArrivedWayPoint(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onGpsOpenStatus(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onNaviInfoUpdate(p0: NaviInfo?) {
        TODO("Not yet implemented")
    }

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {
        TODO("Not yet implemented")
    }

    override fun updateIntervalCameraInfo(
        p0: AMapNaviCameraInfo?,
        p1: AMapNaviCameraInfo?,
        p2: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {
        TODO("Not yet implemented")
    }

    override fun showCross(p0: AMapNaviCross?) {
        TODO("Not yet implemented")
    }

    override fun hideCross() {
        TODO("Not yet implemented")
    }

    override fun showModeCross(p0: AMapModelCross?) {
        TODO("Not yet implemented")
    }

    override fun hideModeCross() {
        TODO("Not yet implemented")
    }

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun showLaneInfo(p0: AMapLaneInfo?) {
        TODO("Not yet implemented")
    }

    override fun hideLaneInfo() {
        TODO("Not yet implemented")
    }

    override fun onMapTypeChanged(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onNaviDirectionChanged(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onDayAndNightModeChanged(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onBroadcastModeChanged(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onScaleAutoChanged(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getCustomMiddleView(): View {
        TODO("Not yet implemented")
    }

    override fun getCustomNaviView(): View {
        TODO("Not yet implemented")
    }

    override fun getCustomNaviBottomView(): View {
        TODO("Not yet implemented")
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return mListener.getInfoWindow(p0)
    }

    override fun getInfoContents(p0: Marker?): View {
        return mListener.getInfoContents(p0)
    }


}