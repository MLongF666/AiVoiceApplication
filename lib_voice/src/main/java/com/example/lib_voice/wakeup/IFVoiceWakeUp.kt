package com.example.lib_voice.wakeup

import android.content.Context

import android.util.Log
import com.example.lib_voice.R
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.iflytek.cloud.VoiceWakeuper
import com.iflytek.cloud.WakeuperListener
import com.iflytek.cloud.util.ResourceUtil
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE

/**
 * @description: TODO 讯飞唤醒
 * @author: mlf
 * @date: 2024/7/14 15:42
 * @version: 1.0
 */
object IFVoiceWakeUp {
    // 语音唤醒对象
    private  var mIvw: VoiceWakeuper? = null
    fun initWakeUp(mContext: Context,ivwPath: String,listener: WakeuperListener) {
        init(mContext)
        //ivePath:mContext.getExternalFilesDir("msc").getAbsolutePath() + "/ivw.wav"
        mIvw = VoiceWakeuper.createWakeuper(mContext, null)
        Log.d("IFVoiceWakeUp", "initWakeUp: $mIvw")

        mIvw=VoiceWakeuper.getWakeuper()
        if (mIvw!=null) {
            // 清空参数
            mIvw!!.setParameter(SpeechConstant.PARAMS, null)

            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw!!.setParameter(SpeechConstant.IVW_THRESHOLD, "1450")

            // 设置唤醒模式
            mIvw!!.setParameter(SpeechConstant.IVW_SST, "wakeup")

            // 设置持续进行唤醒
            mIvw!!.setParameter(SpeechConstant.KEEP_ALIVE, "1")

            // 设置闭环优化网络模式
            mIvw!!.setParameter(SpeechConstant.IVW_NET_MODE, "0")

            // 设置唤醒资源路径
            mIvw!!.setParameter(SpeechConstant.IVW_RES_PATH, getResource(mContext))

            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw!!.setParameter(
                SpeechConstant.IVW_AUDIO_PATH,
                ivwPath
            )
            mIvw!!.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        }

        startWakeUp(listener)

    }

    private fun init(mContext: Context) {
        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用“,”分隔。
        // 设置你申请的应用appid
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        val param = StringBuffer()
        param.append("appid=" + mContext.getString(R.string.app_id))
        param.append(",")

        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC)
        SpeechUtility.createUtility(mContext, param.toString())
    }

     fun startWakeUp(listener: WakeuperListener) {
        if (mIvw!=null) {
            mIvw!!.startListening(listener)
        }
    }
    fun stopWakeUp() {
        if (mIvw!=null) {
            mIvw!!.stopListening()
        }
    }
    private fun getResource(mContext: Context): String {
        val resPath = ResourceUtil.generateResourcePath(
            mContext,
            RESOURCE_TYPE.assets,
            "ivw/" + mContext.getString(R.string.app_id) + ".jet"
        )
        Log.d("TAG", "resPath: $resPath")
        return resPath
    }
}