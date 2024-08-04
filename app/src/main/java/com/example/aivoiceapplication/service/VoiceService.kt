package com.example.aivoiceapplication.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.aivoiceapplication.R
import com.example.aivoiceapplication.adapter.ChatListAdapter
import com.example.aivoiceapplication.data.ChatListData
import com.example.aivoiceapplication.entity.AppConstants
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.SoundPoolHelper
import com.example.lib_base.helper.WindowsHelper
import com.example.lib_base.helper.`fun`.AppHelper
import com.example.lib_base.helper.`fun`.CommonSettingHelper
import com.example.lib_base.helper.`fun`.ConsTellHelper
import com.example.lib_base.helper.`fun`.ContactHelper
import com.example.lib_base.utils.L
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.AiRootBean
import com.example.lib_network.bean.JokeDataBean
import com.example.lib_network.bean.WeatherDataBean
import com.example.lib_voice.engine.VoiceEngineAnalyze
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTs
import com.example.lib_voice.words.WordsTools

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @description: TODO 语音服务
 * @author: mlf
 * @date: 2024/6/25 10:19
 * @version: 1.0
 */
class VoiceService : Service(), OnNluResultListener {

    private lateinit var  textViewTips:TextView
    private lateinit var  mLottieAnimationView:LottieAnimationView
    private var chatListAdapter: ChatListAdapter? = null
    private val mHandler = Handler()
    private val TAG = VoiceService::class.java.simpleName
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    //START_STICKY 保证服务一直运行，直到被系统杀死
    //START_NOT_STICKY 默认值，服务被系统杀死，不会重启
    //START_REDELIVER_INTENT 重启服务，但会保留最后一次的Intent
    //STOP_FOREGROUND_SERVICE 停止前台服务
    //START_STICKY_COMPATIBILITY 兼容模式，保证服务一直运行，直到被系统杀死，但不保证服务被杀死时能及时停止
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        L.i("启动服务 on start command")
        bindNotification()
        return START_STICKY_COMPATIBILITY
    }

    override fun onCreate() {
        initCoreVoiceService()
//        showWindow()
        super.onCreate()
    }
    private lateinit var mFullWindowsView:View
    private lateinit var mChatListView:RecyclerView
    private var mChatList= ArrayList<ChatListData>()
    private fun initCoreVoiceService() {
        WindowsHelper.initHelper(this)
        mFullWindowsView=WindowsHelper.getView(R.layout.layout_windows_item)
        mChatListView = mFullWindowsView.findViewById<RecyclerView>(R.id.mChatListView)
        mLottieAnimationView = mFullWindowsView.findViewById<LottieAnimationView>(R.id.mLottieView)
        textViewTips = mFullWindowsView.findViewById<TextView>(R.id.tvVoiceTips)
        mFullWindowsView.findViewById<ImageView>(R.id.ivCloseWindow).setOnClickListener {
            hideWindow()
        }
        var layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd=true
        mChatListView.layoutManager= layoutManager

        chatListAdapter = ChatListAdapter(mChatList)
        mChatListView.adapter= chatListAdapter
        VoiceManager.initManager(this,getExternalFilesDir("msc")?.absolutePath + "/ivw.wav" ,object : OnAsrResultListener {
            //准备就绪
            override fun weakUpReady() {
                VoiceManager.ttsStart("唤醒引擎准备就绪")
            }

            override fun asrStartSpeak() {
                L.i("开始说话")
            }

            override fun asrStopSpeak() {
                L.i("结束说话")
//                hideWindow()
            }

            override fun weakUpSuccess(result: JSONObject) {
                L.i("唤醒成功${result}")
                val errorCode = result.optInt("errorCode")
                if (errorCode == 0) {
                    //唤醒成功
                    wakeUpFix()
                }
            }

            override fun weakUpError(text: String) {
                L.i("唤醒失败${text}")
                hideWindow()
            }

            override fun asrResult(result: JSONObject) {
                L.i("在线识别结果${result}")
            }

            override fun nluResult(result: JSONObject) {
                L.i("语义识别结果${result}")
                val text = result.optString("raw_text")
                addMineText(text)
                //识别指令
                VoiceEngineAnalyze.analyzeNlu(result, this@VoiceService)
            }

            override fun updateUserText(text: String) {
                updateTips(text)
            }
        })
    }
    private fun wakeUpFix() {
        showWindow()
        updateTips(getString(R.string.text_voice_wakeup_tips))
        //应答
        val wakeupText = WordsTools.wakeupWords()
        addAiText(wakeupText,object :VoiceTTs.OnTTSResultListener{
            override fun onTTEnd() {
                //开始识别
                VoiceManager.startAsr()
            }
        })
    }


    //绑定通知栏
    private fun bindNotification() {
        L.i("绑定通知栏")
        startForeground(1000,
            NotificationHelper.bindVoiceService("正在运行..."))
    }

    override fun queryWeather(city: String) {
        HttpManager.run {
            queryWeather(city,object :Callback<WeatherDataBean>{
                override fun onResponse(p0: Call<WeatherDataBean>, response: Response<WeatherDataBean>) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            it.result.realtime.apply {
                                //填充数据
                                addWeatherText(city,info,temperature,wid,object :VoiceTTs.OnTTSResultListener{
                                    override fun onTTEnd() {
                                        hideWindow()
                                    }
                                })
                            }
                        }
                    }
                }

                override fun onFailure(p0: Call<WeatherDataBean>, p1: Throwable) {
                    addAiText("查询${city}的天气失败")
                    hideWindow()
                }
            })
        }
    }

    override fun queryWeatherInfo(city: String) {
        addAiText("正在为您查询:${city}的天气详情",object :VoiceTTs.OnTTSResultListener{
            override fun onTTEnd() {
                hideWindow()
            }
        })
        ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER,"city",city)

    }


    override fun nluError() {
        L.i("语义识别失败")
        addAiText(WordsTools.noSupportWords())
        hideWindow()
    }

    override fun openApp(appName: String) {
        L.i("打开应用${appName}")
        val isOpen = AppHelper.launcherApp(appName)
        if (isOpen) {
            addAiText(getString(R.string.text_voice_app_open, appName))
        } else {
            addAiText(getString(R.string.text_voice_app_not_open, appName))
        }
        hideWindow()
    }

    private fun showWindow(){
        L.i("=======显示窗口=======")
        WindowsHelper.showView(mFullWindowsView)
        mLottieAnimationView.playAnimation()
    }
    private fun hideWindow(){
        L.i("========隐藏窗口======")
        mHandler.postDelayed({
            WindowsHelper.hideView(mFullWindowsView)
            mLottieAnimationView.pauseAnimation()
            SoundPoolHelper.play(R.raw.record_over)
        },500)
    }


    //添加天气
    private fun addWeatherText(city: String,info:String,temperature:String,wid: String,
                               mOnTTSResultListener: VoiceTTs.OnTTSResultListener) {
        val bean = ChatListData(AppConstants.TYPE_WEATHER_TEXT)
        bean.city = city
        bean.info = info
        bean.temperature = "$temperature°C"
        bean.wid = wid
        addBaseText(bean)
        val text=city+"的天气"+info+temperature+"°C"
        VoiceManager.ttsStart(text, mOnTTSResultListener)
    }

    private fun addMineText(text:String){
        val bean = ChatListData(AppConstants.TYPE_MINE_TEXT)
        bean.text=text
        addBaseText(bean)
    }
    private fun addAiText(text: String) {
        val bean = ChatListData(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        addBaseText(bean)
        VoiceManager.ttsStart(text)
    }
    /**
     * 添加AI文本
     */
    private fun addAiText(text: String, mOnTTSResultListener: VoiceTTs.OnTTSResultListener) {
        val bean = ChatListData(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        addBaseText(bean)
        VoiceManager.ttsStart(text, mOnTTSResultListener)
    }
    private fun addBaseText(bean:ChatListData){
        mChatList.add(bean)
        chatListAdapter?.notifyItemInserted(mChatList.size-1)
        //滑动到最后一条元素
        mChatListView.scrollToPosition(chatListAdapter!!.itemCount -1);//此句为设置显示
    }

    private fun updateTips(text:String){
        L.i("更新提示语${text}")
        textViewTips.text=text
    }

    override fun unInstallApp(appName: String) {
        L.i("deletedApp:$appName")
        val installApp = AppHelper.unInstallApp(appName)
        if (installApp) {
            addAiText(getString(R.string.text_voice_app_uninstall, appName))
        } else {
            addAiText(getString(R.string.text_voice_app_not_uninstall, appName))
        }
        hideWindow()
    }
    override fun otherApp(appName: String) {
        //跳转应用商店
        var isStore = AppHelper.launcherAppStore(appName)
        if (isStore) {
            addAiText(getString(R.string.text_voice_app_option, appName))
        }else{
            addAiText(WordsTools.noAnswerWords())
        }
    }

    override fun back(){
        L.i("返回back")
        CommonSettingHelper.back()
    }

    override fun home(): Any? {
        TODO("Not yet implemented")
    }

    override fun setVolumeUp(): Any? {
        TODO("Not yet implemented")
    }

    override fun setVolumeDown(): Any? {
        TODO("Not yet implemented")
    }

    override fun quit(): Any? {
        TODO("Not yet implemented")
    }

    //拨打联系人
    override fun callPhoneForName(name: String) {
        var list =ContactHelper.getContactList().filter { it.phoneName == name}
        if (list.isNotEmpty()) {
            L.i("拨打联系人列表${list}")
            addAiText("正在为您拨打联系人:$name",object :VoiceTTs.OnTTSResultListener{
                override fun onTTEnd() {
                    ContactHelper.callPhone(list[0].phoneNumber)
                }
            })
        }else{
            addAiText(getString(R.string.text_voice_no_friend))
        }
        hideWindow()
    }
    //拨打号码
    override fun callPhoneForNumber(phone: String) {
        addAiText("正在为您拨打号码:$phone",object :VoiceTTs.OnTTSResultListener{
            override fun onTTEnd() {
                ContactHelper.callPhone(phone)
                hideWindow()
            }

        })
    }

    //播放笑话
    override fun playJoke(){
        L.i("播放笑话")
        HttpManager.getJoke(object :Callback<JokeDataBean>{
            override fun onResponse(p0: Call<JokeDataBean>, p1: Response<JokeDataBean>) {
                p1.body()?.let {
                    if (it.code==200){
                        val item = it.result.list[0]
                        L.i("笑话内容${item.content}")
                        addAiText(item.content,object :VoiceTTs.OnTTSResultListener{
                            override fun onTTEnd() {
                                hideWindow()
                            }
                        })
                    }else{
                        jokeError()
                    }
                }
            }

            override fun onFailure(p0: Call<JokeDataBean>, p1: Throwable) {
                L.i("笑话列表失败${p1.message}")
                jokeError()
            }

        })
    }

    override fun jokeList(){
        addAiText("正在为您搜索笑话")
        ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
        hideWindow()
    }

    override fun conTellTime(word: String) {
        L.i("查询时间${word}")
        val tellTime = ConsTellHelper.getConsTellTime(word)
        addAiText(tellTime,object :VoiceTTs.OnTTSResultListener{
            override fun onTTEnd() {
                hideWindow()
            }

        })
    }

    override fun conTellInfo(word: String) {
        L.i("查询信息${word}")
        ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION,"word",word)
    }

    override fun routeMap(word: String) {
        TODO("Not yet implemented")
    }

    override fun nearByMap(word: String) {
        TODO("Not yet implemented")
    }

    override fun aiRobot(string: String) {
        //请求机器人回答
        HttpManager.queryRobot(string,object :Callback<AiRootBean>{
            override fun onResponse(p0: Call<AiRootBean>, p1: Response<AiRootBean>) {
                p1.body()?.let {
                    L.i("机器人回答${it.content}")
                        addAiText(it.content,object :VoiceTTs.OnTTSResultListener{
                            override fun onTTEnd() {
                                hideWindow()
                            }
                        })
                }
            }

            override fun onFailure(p0: Call<AiRootBean>, p1: Throwable) {
                L.e("机器人请求失败${p1.message}")
            }

        })

    }

    private fun jokeError(){
        addAiText(getString(R.string.text_voice_query_joke_error))
        hideWindow()
    }
}