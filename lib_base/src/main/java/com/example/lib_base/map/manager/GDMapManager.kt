package com.example.lib_base.map.manager
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import com.amap.api.navi.AmapPageType
import com.amap.api.navi.INaviInfoCallback
import com.amap.api.navi.enums.TravelStrategy
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
import com.amap.api.navi.model.NaviPoi
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
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
import com.example.lib_base.utils.L


/**
 * @description: TODO 高德地图管理类
 * @author: mlf
 * @date: 2024/8/7 12:04
 * @version: 1.0
 */
@SuppressLint("StaticFieldLeak")
object GDMapManager : GeocodeSearch.OnGeocodeSearchListener, INaviInfoCallback,
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
        //TODO 定位合规检查
        AMapLocationClient.updatePrivacyShow(mContext,true,true)
        AMapLocationClient.updatePrivacyAgree(mContext,true)
        GDMapManager.mMapView = mMapView
        L.i("mMap init success")
        GDMapManager.mMapView!!.onCreate(bundle)
        mMap = GDMapManager.mMapView!!.map
        this.mContext = mContext
        initRouteSearch(mContext)
        initMapLocation(mContext)
    }
    //初始化定位
    private fun initMapLocation(mContext: Context){
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
        mGeocodeSearch = GeocodeSearch(mContext)
        mGeocodeSearch.setOnGeocodeSearchListener(this)
    }
    //设置定位开关
    fun setSwitchLocation(isSwitch: Boolean,mListener: AMapLocationListener?){
        if (!isSwitch){
            mLocationClient.stopLocation()
        }else{
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
            mLocationClient.setLocationListener(mListener)
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
    fun poiSearchKeyWord(keyWord:String,poiType:String,city :String,currentPage:Int){
        query = PoiSearchV2.Query(keyWord,poiType, city)
        query!!.pageSize = 10;// 设置每页最多返回多少条poiitem
        query!!.pageNum = currentPage;//设置查询页码
        val poiSearch = PoiSearchV2(mContext, query)
        poiSearch.setOnPoiSearchListener(this)
        poiSearch.searchPOIAsyn()
    }
    //范围搜索
    fun poiSearchAround(keyWord:String,poiType:String,latLonPoint: LatLng,pageNum:Int,pageSize:Int,radius:Int,mContext: Context){
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
    fun startWalkRouteSearch(mStartPoint: LatLng, mEndPoint: LatLonPoint?, keyword: String?){
        var end=NaviPoi(keyword, null, "")
        end = if (mEndPoint!=null){
            // 构造终点POI
            NaviPoi(keyword, LatLng(mEndPoint.latitude, mEndPoint.longitude), "")
        }else{
            NaviPoi(keyword, null, "")
        }
        // 构造起点POI
        val start = NaviPoi("故宫博物馆",mStartPoint , "B000A8UIN8")

        // 进行骑行算路
        AMapNavi.getInstance(mContext).calculateRideRoute(start, end, TravelStrategy.SINGLE)
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
    fun startWalkNavi(startPoint: LatLng, endPoint: LatLng?, startName:String, endName:String){
        //起点
        val start: Poi = Poi(startName, startPoint, "")
      //途经点
//        val poiList: ArrayList<Poi> = ArrayList<Poi>()
//        poiList.add(Poi(endName, LatLng(39.918058, 116.397026), "B000A8UIN8"))
        //终点
        val end: Poi = Poi(endName, endPoint, "")
        L.i("endName: $endName")
        // 组件参数配置 //最后一个参数，AmapPageType.NAVI为导航界面，AmapPageType.ROUTE为路线规划界面
        val params = AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE)
        params.setNeedDestroyDriveManagerInstanceWhenNaviExit(true) //退出导航后是否释放导航资源
        params.setUseInnerVoice(false)
        params.setMultipleRouteNaviMode(true)
//        // 是否计算路径规划时
//        params.setNeedCalculateRouteWhenPresent(false)
        // 启动组件
        val naviPage = AmapNaviPage.getInstance()
        naviPage.showRouteActivity(mContext.applicationContext, params, this)
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
        L.i("AMapNavilistener onLocationChange ${p0?.coord?.latitude},${p0?.coord?.longitude}")
    }

    override fun onGetNavigationText(p0: Int, p1: String?) {
        L.i("AMapNavilistener onGetNavigationText $p0 $p1")
    }

    override fun onArriveDestination(p0: Boolean) {

    }

    override fun onStartNavi(p0: Int) {
    }

    override fun onTrafficStatusUpdate() {
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        L.i("onCalculateRouteSuccess ${p0?.contentToString()}")
    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
        L.i("onCalculateRouteSuccess ${p0?.toString()}")
    }

    override fun notifyParallelRoad(p0: Int) {
    }

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {
    }

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {
    }

    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {
    }

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {
    }

    override fun onPlayRing(p0: Int) {
    }

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {
    }

    override fun onGpsSignalWeak(p0: Boolean) {
    }

    override fun onCalculateRouteFailure(p0: Int) {
    }

    override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
    }

    override fun onReCalculateRouteForYaw() {
    }

    override fun onReCalculateRouteForTrafficJam() {
    }

    override fun onStopSpeaking() {
    }

    override fun onReCalculateRoute(p0: Int) {
    }

    override fun onExitPage(p0: Int) {
        if (p0 !=null){
            L.i("onExitPage $p0")
        }

    }

    override fun onStrategyChanged(p0: Int) {
    }

    override fun onArrivedWayPoint(p0: Int) {
    }

    override fun onGpsOpenStatus(p0: Boolean) {
    }

    override fun onNaviInfoUpdate(p0: NaviInfo?) {
    }

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {
    }

    override fun updateIntervalCameraInfo(
        p0: AMapNaviCameraInfo?,
        p1: AMapNaviCameraInfo?,
        p2: Int
    ) {
    }

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {
    }

    override fun showCross(p0: AMapNaviCross?) {
    }

    override fun hideCross() {
    }

    override fun showModeCross(p0: AMapModelCross?) {
    }

    override fun hideModeCross() {
    }

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {
    }

    override fun showLaneInfo(p0: AMapLaneInfo?) {
    }

    override fun hideLaneInfo() {
    }

    override fun onMapTypeChanged(p0: Int) {
    }

    override fun onNaviDirectionChanged(p0: Int) {
    }

    override fun onDayAndNightModeChanged(p0: Int) {
        Log.i("TAG","")
    }

    override fun onBroadcastModeChanged(p0: Int) {
    }

    override fun onScaleAutoChanged(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getCustomMiddleView(): View? {
        return null
    }

    override fun getCustomNaviView(): View? {
        return null
    }

    override fun getCustomNaviBottomView(): View? {
        return null
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return mListener.getInfoWindow(p0)
    }

    override fun getInfoContents(p0: Marker?): View {
        return mListener.getInfoContents(p0)
    }
}