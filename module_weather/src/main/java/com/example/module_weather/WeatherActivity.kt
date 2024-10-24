package com.example.module_weather

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.L
import com.example.lib_base.utils.SpUtil
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.WeatherDataBean
import com.example.module_weather.databinding.ActivityWeatherBinding
import com.example.module_weather.tools.WeatherIconTools
import com.example.module_weather.ui.CityActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    private lateinit var mLineChart : LineChart
    private val CODE_SELECT_CITY = 1000
    override fun getTitleText(): String {
        return getString(R.string.app_name)
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

    override fun initViewBinding(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        L.d("initView:")
        this.intent.run {
            val city = getStringExtra("city")
            if (city != null) {
                loadWeather(city)
            }else{
                if (SpUtil.get("city", "").toString().isEmpty()){
                    loadWeather("北京")
                }else{
                    loadWeather(SpUtil.get("city", "").toString())
                }
            }
        }
    }

    private fun loadWeather(city: String) {
        SpUtil.put("city", city)
        supportActionBar?.title = city
        initChart()
        L.d("loadWeather:$city")
        HttpManager.queryWeather(city, object : Callback<WeatherDataBean>{
            override fun onResponse(p0: Call<WeatherDataBean>, p1: Response<WeatherDataBean>) {
                val body = p1.body()
                L.d("onResponse")
                p1.isSuccessful.run {
                    body?.let {
                        if (it.error_code == 10012) {
                            //超过每日可允许的次数了
                            return
                        }
                        it.result.realtime.apply {
                            //设置天气
                            getBinding().mInfo.text=info
                            //设置图片
                            getBinding().mIvWid.setImageResource(WeatherIconTools.getWeatherIcon(wid))
                            //设置温度
                            getBinding().mTemperature.text =
                                String.format(
                                    "%s%s",
                                    temperature,
                                    getString(com.example.lib_base.R.string.app_weather_t)
                                )
                            //设置湿度
                            getBinding().mHumidity.text =
                                String.format(
                                    "%s\t%s",
                                    getString(com.example.lib_base.R.string.app_weather_humidity),
                                    humidity
                                )
                            //设置风向
                            getBinding().mDirect.text =
                                String.format(
                                    "%s\t%s",
                                    getString(com.example.lib_base.R.string.app_weather_direct),
                                    direct
                                )
                            //设置风力
                            getBinding().mPower.text =
                                String.format(
                                    "%s\t%s",
                                    getString(com.example.lib_base.R.string.app_weather_power),
                                    power
                                )
                            //设置空气质量
                            getBinding().mAqi.text = String.format(
                                "%s\t%s",
                                getString(com.example.lib_base.R.string.app_weather_aqi),
                                aqi
                            )
                            val data = ArrayList<Entry>()
                            //绘制图表
                            it.result.future.forEachIndexed{index, future->
                                var temp = future.temperature.substring(0, 2)
                                if (temp.contains("/")){
                                    temp=temp.replace("/","")
                                }
                                data.add(Entry((index + 1).toFloat(),temp.toFloat()))
                            }
                            setLineChartData(data)
                        }
                    }
                }

            }

            override fun onFailure(p0: Call<WeatherDataBean>, p1: Throwable) {
                L.d("天气数据失败:${p1}")
            }

        })
    }
    //设置图标数据
    private fun setLineChartData(values: java.util.ArrayList<Entry>) {

        if (mLineChart.data != null && mLineChart.data.dataSetCount > 0) {
            //获取数据容器
            val set = mLineChart.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            mLineChart.data.notifyDataChanged()
            mLineChart.notifyDataSetChanged()
            //如果存在数据才这样去处理
        } else {
            val set = LineDataSet(values, getString(R.string.text_chart_tips_text))

            //=============================UI配置=============================
            set.enableDashedLine(10f, 10f, 0f)
            set.setCircleColor(Color.BLACK)
            set.lineWidth = 1f
            set.circleRadius = 3f
            set.setDrawCircleHole(false)
            set.valueTextSize = 10f
            set.formLineWidth = 1f
            set.setDrawFilled(true)
            set.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set.formSize = 15f

            set.fillColor = Color.YELLOW

            //设置数据
            val dataSet = ArrayList<LineDataSet>()
            dataSet.add(set)
            val data = LineData(dataSet as List<ILineDataSet>?)
            mLineChart.data = data
        }

        //=============================UI配置=============================

        //X轴动画
        mLineChart.animateX(2000)
        //刷新
        mLineChart.invalidate()
        //页眉
        val legend = mLineChart.legend
        legend.form = Legend.LegendForm.LINE
    }
    //初始化图表
    private fun initChart() {
        //=============================基本配置=============================
        mLineChart = getBinding().mLineChart
        //后台绘制
        mLineChart.setDrawGridBackground(true)
        //开启描述文本
        mLineChart.description.isEnabled = true
        mLineChart.description.text = getString(R.string.text_ui_tips)
        //触摸手势
        mLineChart.setTouchEnabled(true)
        //支持缩放
        mLineChart.setScaleEnabled(true)
        //拖拽
        mLineChart.isDragEnabled = true
        //扩展缩放
        mLineChart.setPinchZoom(true)

        //=============================轴配置=============================

        //平均线
        val xLimitLine = LimitLine(10f, "")
        xLimitLine.lineWidth = 4f
        xLimitLine.enableDashedLine(10f, 10f, 0f)
        xLimitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        xLimitLine.textSize = 10f

        val xAxis = mLineChart.xAxis
        xAxis.enableAxisLineDashedLine(10f, 10f, 0f)
        //最大值
        xAxis.mAxisMaximum = 5f
        //最小值
        xAxis.axisMinimum = 1f

        val axisLeft = mLineChart.axisLeft
        axisLeft.enableAxisLineDashedLine(10f, 10f, 0f)
        //最大值
        axisLeft.mAxisMaximum = 40f
        //最小值
        axisLeft.axisMinimum = 20f

        //禁止右边的Y轴
        mLineChart.axisRight.isEnabled = false
    }
    //显示右上角菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_city, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_city){
            //跳转到城市选择页面
            startActivityCity(CityActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startActivityCity(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivityForResult(intent, CODE_SELECT_CITY)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        L.d("onActivityResult:$requestCode,$resultCode")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_SELECT_CITY) {
                data?.let {
                    val city = it.getStringExtra("city")
                    if (!TextUtils.isEmpty(city)) {
                        loadWeather(city!!)
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        L.d("WeatherActivity onRestart")
    }

}