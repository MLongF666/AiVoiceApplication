package com.example.aivoiceapplication
import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.aivoiceapplication.data.MainListData
import com.example.aivoiceapplication.databinding.ActivityMainBinding
import com.example.aivoiceapplication.service.VoiceService
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.base.impl.OnItemClick
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.`fun`.ContactHelper
import com.example.lib_base.trasformer.ScaleInTransformer
import com.example.lib_base.utils.L
import com.example.lib_voice.helper.AudioManagerHelper
import com.example.lib_voice.helper.VolumeChangeObserver

class MainActivity : BaseActivity<ActivityMainBinding>(),VolumeChangeObserver.VolumeChangeListener,VoiceService.OnVoiceListener {
    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private var mVoiceService: VoiceService?=null
    private var arrayList = ArrayList<String>()
    private var mList = ArrayList<MainListData>()
    private lateinit var mVolumeChangeObserver: VolumeChangeObserver
    override fun getTitleText(): String {
        return "AI语音助手"
    }

    override fun initEvent() {
        getBinding().buttonVoice.setOnClickListener {
            mVoiceService?.wakeUpFix()
        }
        mVolumeChangeObserver.volumeChangeListener = this
        AudioManagerHelper.setOnMyAudioFocusChangeListener(object :AudioManagerHelper.OnMyAudioFocusChangeListener{
            override fun setVolume(volume: Int){
                L.d("getVolume MainActivity: $volume")
            }

            override fun getStreamType(): Int {
                return AudioManager.STREAM_VOICE_CALL
            }

        })
    }
    override fun initData() {
        val arrayMainTitles = resources.getStringArray(com.example.lib_base.R.array.MainTitleArray)

        arrayMainTitles.forEach {
            arrayList.add(it)
        }

    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        mVolumeChangeObserver.registerReceiver()
    }
    override fun onPause() {
        super.onPause()
        mVolumeChangeObserver.unregisterReceiver()
    }
    override fun initView() {
        mVolumeChangeObserver = VolumeChangeObserver(this)
        //动态权限
        if (checkPermission(permissions)) {
            linkService()
        } else {
            requestPermission(
                permissions
            ) { linkService() }
        }
        //窗口权限
        if (!checkWindowPermission()) {
            requestWindowPermission(packageName)
        }
        initPageData()
        initPageView()
    }

    private fun initPageView() {
        val height = windowManager.defaultDisplay.height
        val commonAdapter = CommonAdapter<MainListData>(mList,
            object : CommonAdapter.OnBindDataListener<MainListData> {
                override fun onBindViewHolder(
                    model: MainListData,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    val layout = viewHolder.getView<LinearLayout>(R.id.bg)
                    val cardView = viewHolder.getView<CardView>(R.id.card)
                    cardView.setCardBackgroundColor(model.color)
                    layout.layoutParams?.let { lp ->
                        lp.height = height / 5 * 3
                    }
                    val textView = viewHolder.getView<TextView>(R.id.text)
                    textView.text = model.title
                    val icon = viewHolder.getView<ImageView>(R.id.icon)
                    icon.setImageResource(model.icon)
                    val description = viewHolder.getView<TextView>(R.id.description)
                    description.text = model.description
                    //设置text一行展示 省略号
                    if (model.description.length > 10) {
                        description.ellipsize = android.text.TextUtils.TruncateAt.END
                        description.maxLines = 1
                    }
                }
                override fun getLayoutId(type: Int): Int {
                    return R.layout.item_main_list
                }
            })
        getBinding().viewPager.adapter = commonAdapter
        getBinding().viewPager.offscreenPageLimit = mList.size
        val transformer = CompositePageTransformer()
        transformer.addTransformer(ScaleInTransformer())
        transformer.addTransformer(MarginPageTransformer(70))
        getBinding().viewPager.setPageTransformer(transformer)
        commonAdapter.setOnItemClick(object : OnItemClick<MainListData> {
            override fun onItemClick(position: Int, view: View, t: MainListData) {
//                Toast.makeText(this@MainActivity, t.title, Toast.LENGTH_SHORT).show()
                when (t.title) {
                    "应用管理" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
                    }

                    "开发者模式" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
                    }

                    "天气" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
                    }

                    "星座" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
                    }

                    "笑话" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
                    }

                    "地图" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
                    }

                    "语音设置" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
                    }

                    "系统设置" -> {
                        ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
                    }
                    else -> {
                        return
                    }
                }
            }
        })
    }
    override fun onStop() {
        super.onStop()
    }
    @SuppressLint("Recycle")
    private fun initPageData() {
        //获取页面数据
        val titleArray = resources.getStringArray(com.example.lib_base.R.array.MainTitleArray)
        val colorsArray = resources.getIntArray(R.array.MainColorArray)
        val iconsArray = resources.obtainTypedArray(R.array.MainIconArray)
        val descriptionArray = resources.getStringArray(R.array.MainDescriptionArray)
        for ((index, value) in titleArray.withIndex()) {
            mList.add(
                MainListData(
                    value, colorsArray[index],
                    iconsArray.getResourceId(index, 0),
                    descriptionArray[index]
                )
            )
        }
    }
    private fun linkService() {
        ContactHelper.init(this)
        val intent = Intent(this, VoiceService::class.java)
        startService(intent)
        bindService(intent, MyConnection(), Context.BIND_AUTO_CREATE)
    }
    internal inner class MyConnection : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as VoiceService.LocalBinder
            mVoiceService = binder.getService()
            mVoiceService?.setOnVoiceListener(this@MainActivity)

        }

    }
    override fun onVolumeChanged(volume: Int) {
        L.d("onVolumeChanged MainActivity: $volume")
    }

    override fun setAmplitude(a: Int) {
        getBinding().waveView.putValue(a)
        getBinding().waveView.invalidate()
    }
}