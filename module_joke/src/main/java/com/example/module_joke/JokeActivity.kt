package com.example.module_joke
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.base.impl.OnItemClick
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.utils.L
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.Item
import com.example.lib_network.bean.JokeDataBean
import com.example.lib_network.bean.JokeResult
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTs
import com.example.module_joke.databinding.ActivityJokeBinding
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.coroutines.selects.whileSelect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Route(path = ARouterHelper.PATH_JOKE)
class JokeActivity : BaseActivity<ActivityJokeBinding>(){
    private lateinit var rvJokeList: RecyclerView
    private lateinit var mJokeList: ArrayList<Item>
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mJokeAdapter: CommonAdapter<Item>
    private var currentPlayIndex: Int = -1
    override fun getTitleText(): String {
        return "笑话"
    }


    override fun initEvent() {
//刷新
        mRefreshLayout.setOnRefreshListener {
            loadData(0)
            it.finishRefresh()
        }
        //加载
        mRefreshLayout.setOnLoadMoreListener {
            loadData(1)
            it.finishLoadMore()
        }
    }

    override fun initData() {
        mJokeList = ArrayList<Item>()
        rvJokeList = getBinding().rvJokeList
        rvJokeList.layoutManager = LinearLayoutManager(this)
        mJokeAdapter = CommonAdapter(mJokeList,object : CommonAdapter.OnBindDataListener<Item> {
            override fun onBindViewHolder(
                model: Item,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                val textView = viewHolder.getView<TextView>(R.id.tv_joke)
                textView.text = model.content
                val imageButton = viewHolder.getView<ImageButton>(R.id.btn_switch)
                imageButton.setOnClickListener {
                    if (imageButton.isSelected&&currentPlayIndex == position){
//                        Toast.makeText(this@JokeActivity,"已关闭语音",Toast.LENGTH_SHORT).show()
                        VoiceManager.ttsPause()
                        currentPlayIndex = -1
                    }else{
                        val oldIndex = currentPlayIndex
                        currentPlayIndex = position
                        VoiceManager.ttsStop()
//                        Toast.makeText(this@JokeActivity,"已开启语音",Toast.LENGTH_SHORT).show()
                        VoiceManager.ttsStart(model.content,object : VoiceTTs.OnTTSResultListener{
                            override fun onTTEnd() {
                                currentPlayIndex = -1
                                imageButton.isSelected = false
                            }

                        })
                        mJokeAdapter.notifyItemChanged(oldIndex)
                    }
                    imageButton.isSelected = !imageButton.isSelected
                }
            }

            override fun getLayoutId(type: Int): Int {
                return R.layout.item_joke
            }

        })
        rvJokeList.adapter = mJokeAdapter
        loadData(0)
    }

    override fun initViewBinding(): ActivityJokeBinding {
        return ActivityJokeBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        VoiceManager.ttsPause()
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        mRefreshLayout = getBinding().refreshLayout
        mRefreshLayout.setRefreshFooter(BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale))
        mRefreshLayout.setRefreshHeader(MaterialHeader(this))
//        mRefreshLayout.setRefreshHeader(ClassicsHeader(this))
//        mRefreshLayout.setRefreshFooter(ClassicsFooter(this))
        mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能

    }
    //加载数据  0 下拉刷新 1 加载更多
    private fun loadData(orientation: Int) {
        HttpManager.getJokeList(object : Callback<JokeDataBean> {
            override fun onResponse(p0: Call<JokeDataBean>, p1: Response<JokeDataBean>) {
                val result = p1.body()?.result
                val list = result?.list
                L.i("joke: $list")
                when (orientation) {
                    0 -> {
                        //刷新数据
                        mJokeList.clear()
                        list?.let { mJokeList.addAll(it) }
                        //适配器刷新
                        mJokeAdapter.notifyDataSetChanged()
                    }
                    1 -> {
                        //追加数据
                        mJokeList.addAll(list!!)
                        //局部刷新
                        mJokeAdapter.notifyItemRangeChanged(mJokeList.size-list.size, list.size)
                    }
                }
            }

            override fun onFailure(p0: Call<JokeDataBean>, p1: Throwable) {
                L.e("joke: $p1")
                when (orientation) {
                    0 -> {
                        mRefreshLayout.finishRefresh()
                    }
                    1 -> {
                        mRefreshLayout.finishLoadMore()
                    }
                }
            }

        })
    }
}