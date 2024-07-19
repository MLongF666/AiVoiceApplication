package com.example.module_weather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.module_weather.data.CityInfo
import com.example.module_weather.data.CityItemItem
import com.example.module_weather.databinding.ActivityCityBinding
import com.example.module_weather.tools.CityHelper
import com.example.module_weather.view.SideBar

class CityActivity : BaseActivity<ActivityCityBinding>() {
    private lateinit var cityInfo: CityInfo
    override fun getTitleText(): String {
        return "城市选择"
    }
    override fun initEvent() {
        getBinding().citySidebar.setOnTouchingLetterChangedListener(object : SideBar.OnTouchingLetterChangedListener {
            override fun onTouchingLetterChanged(s: String?) {
                Toast.makeText(this@CityActivity, s, Toast.LENGTH_SHORT).show()
            }

            override fun onTouchingLetterChanging(s: String?) {
                Toast.makeText(this@CityActivity, s, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun initData() {
        initCityList()
    }

    private fun initCityList() {
        cityInfo = CityHelper.initHeaderCity()
        getBinding().cityList.adapter = CommonAdapter(cityInfo, object : CommonAdapter.OnBindDataListener<CityItemItem> {
            override fun onBindViewHolder(
                model: CityItemItem,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {

            }

            override fun getLayoutId(type: Int): Int {
                TODO("Not yet implemented")
            }

        })
    }

    override fun initViewBinding(): ActivityCityBinding {
        return ActivityCityBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {

    }
}