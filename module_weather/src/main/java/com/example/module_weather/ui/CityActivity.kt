package com.example.module_weather.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.base.bean.City

import com.example.lib_base.utils.AssetUtils
import com.example.lib_base.utils.L
import com.example.module_weather.R
import com.example.module_weather.data.CitySelectData

import com.example.module_weather.databinding.ActivityCityBinding
import com.example.module_weather.view.CitySelectView
import kotlin.system.exitProcess

class CityActivity : BaseActivity<ActivityCityBinding>() {
    private val mList = ArrayList<CitySelectData>()
    private lateinit var mCitySelectAdapter: CommonAdapter<CitySelectData>
    private lateinit var mCityList: RecyclerView
    //标题
    private val mTypeTitle = 1000
    //热门城市
    private val mTypeHot = 1001
    //内容
    private val mTypeContent = 1002
    override fun getTitleText(): String {
        return "城市选择"
    }

    override fun initEvent() {
        getBinding().citySelectView.setOnViewResultListener(object :CitySelectView.OnViewResultListener{
            override fun uiChange(show: Boolean) {
                getBinding().citySelectViewText.visibility=if(show) View.VISIBLE else View.GONE
            }

            override fun valueInput(value: String) {
                getBinding().citySelectViewText.text=value
                //计算值
                findTextIndex(value)
            }

        })
        getBinding().cityList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //计算滚动到的条目是哪一个
                val lm = recyclerView.layoutManager
                if (lm is LinearLayoutManager){
                    // 0 - 12 12 24
                    val firstPosition = lm.findFirstVisibleItemPosition()
//                    val lastPosition = lm.findLastVisibleItemPosition()
                    //取顶部值 得到省份
                    val province = mList[firstPosition].province
                    val itemIndex = mListTitle.indexOf(province)
                    //防止数组越界
                    if (itemIndex!=-1&&mListTitle.size>itemIndex){
                        getBinding().citySelectView.setCityIndex(itemIndex)
                    }

                }
            }
        })
        getBinding().citySelectView.setCityIndex(0)
    }

    //根据城市寻找下标
    private fun findTextIndex(value: String) {
        if (mList.size>0){
            mList.forEachIndexed { index, citySelectData ->
                if (value==citySelectData.title){

                    getBinding().cityList.scrollToPosition(index)
                    return@forEachIndexed
                }

            }

        }

    }

    override fun initData() {
        val city = AssetUtils.getCity()
        initCityList(city)


    }

    private val mListTitle = ArrayList<String>()
    private fun initCityList(city: City) {
        mCityList = getBinding().cityList
        addTitle("热门")
        addHotCity()
        city.result.forEach {
            it.apply {
                //如果不包含省份 则需要创建省份标题
                if (!mListTitle.contains(province)){
                    addTitle(province)
                }
                addContent(it.city+it.district,it.city,it.province)
            }
        }
        getBinding().citySelectView.setCityList(mListTitle)
        mCityList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mCitySelectAdapter=CommonAdapter(mList,object :CommonAdapter.OnMoreBindDataListener<CitySelectData>{
            override fun onBindViewHolder(
                model: CitySelectData,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                when (type) {
                    mTypeTitle->{
                        viewHolder.setText(R.id.tv_title,model.title)
                    }
                    mTypeHot->{
                        val recyclerView = viewHolder.getView<RecyclerView>(R.id.rv_city_hot)
                        setHotCityView(recyclerView)
                    }
                    mTypeContent->{
                        viewHolder.setText(R.id.city_name,model.content)
                        viewHolder.itemView.setOnClickListener {
                            Toast.makeText(this@CityActivity,"选择城市：${model.city}",Toast.LENGTH_SHORT).show()
                            finishResult(model.city)
                        }
                    }
                }
            }

            override fun getItemViewType(position: Int): Int {
                return mList[position].type
            }

            override fun getLayoutId(type: Int): Int {
                return when (type) {
                    mTypeTitle-> R.layout.layout_city_title
                    mTypeHot-> R.layout.layout_city_hot
                    mTypeContent-> R.layout.layout_city_content
                    else -> 0
                }

            }

        })
        mCityList.adapter = mCitySelectAdapter
    }

    private fun finishResult(city: String) {
        val intent = Intent()
        intent.putExtra("city", city)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setHotCityView(recyclerView: RecyclerView) {
        recyclerView.layoutManager= GridLayoutManager(this,3)
        val array = this.resources.getStringArray(com.example.lib_base.R.array.HotCityArray)
        recyclerView.adapter=CommonAdapter(array.toList(),object :CommonAdapter.OnBindDataListener<String>{
            override fun onBindViewHolder(
                model: String,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                viewHolder.setText(R.id.city_hot_name,model)
                viewHolder.itemView.setOnClickListener {
                    finishResult(model)
                    finish()
                }
            }

            override fun getLayoutId(type: Int): Int {
                return R.layout.item_city_hot
            }


        })
    }

    override fun onDestroy() {
        super.onDestroy()
        L.d("CityActivity onDestroy")
    }

    private fun addContent(content: String,city:String,province:String) {
       mList.add(CitySelectData("", mTypeContent,content,city,province))
    }

    private fun addHotCity() {
        mList.add(CitySelectData("", mTypeHot,"","","热门"))
    }

    private fun addTitle(title: String) {
        mList.add(CitySelectData(title, mTypeTitle,"","",title))
        mListTitle.add(title)
    }

    override fun initViewBinding(): ActivityCityBinding {
        return ActivityCityBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

    }

}