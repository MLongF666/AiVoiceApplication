package com.example.aivoiceapplication.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.aivoiceapplication.R
import com.example.aivoiceapplication.adapter.ChatListAdapter
import com.example.aivoiceapplication.data.ChatListData
import com.example.aivoiceapplication.entity.AppConstants
import com.example.lib_base.helper.NotificationHelper
import com.example.lib_base.helper.SoundPoolHelper
import com.example.lib_base.helper.WindowsHelper
import com.example.lib_base.utils.L
import com.example.lib_voice.engine.VoiceEngineAnalyze
import com.example.lib_voice.impl.OnAsrResultListener
import com.example.lib_voice.impl.OnNluResultListener
import com.example.lib_voice.manager.VoiceManager
import com.iflytek.cloud.WakeuperResult

import org.json.JSONObject

/**
 * @description: TODO 语音服务
 * @author: mlf
 * @date: 2024/6/25 10:19
 * @version: 1.0
 */
class VoiceService : Service(), OnNluResultListener {
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
        mChatListView = mFullWindowsView.findViewById<RecyclerView>(R.id.chat_list_rv)
        mLottieAnimationView = mFullWindowsView.findViewById<LottieAnimationView>(R.id.lottie_animation_view)
        mChatListView.layoutManager=LinearLayoutManager(this)
        chatListAdapter = ChatListAdapter(mChatList)
        mChatListView.adapter= chatListAdapter
        VoiceManager.initManager(this,getExternalFilesDir("msc")?.absolutePath + "/ivw.wav" ,object : OnAsrResultListener {
            //准备就绪
            override fun weakUpReady() {
                L.i("唤醒准备就绪")
                VoiceManager.ttsStart("唤醒引擎准备就绪")
            }

            override fun asrStartSpeak() {
                L.i("开始说话")
            }

            override fun asrStopSpeak() {
                L.i("结束说话")
            }

            override fun weakUpSuccess(result: JSONObject) {
                L.i("唤醒成功${result}")
                //当唤醒词是小爱同学才开始识别
//                VoiceManager.startAsr()
            }

            override fun weakUpSuccess(result: WakeuperResult) {
                var text = result.resultString
                VoiceManager.ttsStart("你好 我在")
            }

            override fun weakUpError(text: String) {
                L.i("唤醒失败${text}")
                //由于唤醒功能暂未成功 所以唤醒失败也开启ASR
                VoiceManager.startAsr()
            }

            override fun asrResult(result: JSONObject) {
                L.i("在线识别结果${result}")
            }

            override fun nluResult(nlu: JSONObject) {
                L.i("语义识别结果${nlu}")
                VoiceEngineAnalyze.analyzeNlu(nlu, this@VoiceService)
            }
        })
    }


    //绑定通知栏
    private fun bindNotification() {
        L.i("绑定通知栏")
        startForeground(1000,
            NotificationHelper.bindVoiceService("正在运行..."))
    }

    override fun queryWeather(city: String, date: String) {
        L.i("查询天气${city} ${date}")
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
        },2000)

    }

    private fun addMineText(text:String){
        var bean = ChatListData(AppConstants.TYPE_MINE_TEXT)
        bean.text=text
        addBaseText(bean)
    }
    private fun addBaseText(bean:ChatListData){
        mChatList.add(bean)
        chatListAdapter?.notifyItemChanged(mChatList.size-1)
    }

}