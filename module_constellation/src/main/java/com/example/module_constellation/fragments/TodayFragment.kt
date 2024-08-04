package com.example.module_constellation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.base.BaseFragment
import com.example.lib_base.utils.L
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.constell.TodayData
import com.example.module_constellation.R
import com.example.module_constellation.databinding.FragmentTodayBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class TodayFragment : BaseFragment<FragmentTodayBinding>() {
    // TODO: Rename and change types of parameters
    private var cid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cid = it.getString("cid")
        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentTodayBinding {
        return FragmentTodayBinding.inflate(inflater)
    }

    override fun intData() {
        HttpManager.queryTodayConstellation(cid!!, object : Callback<TodayData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(p0: Call<TodayData>, p1: Response<TodayData>) {
                p1.body()?.let {
                    L.d("TodayData: ${it.toString()}")
                    if (it.code == 1) {
                        getBinding().sbLove.progress=it.data.love.toInt()
                        getBinding().sbHealth.progress=it.data.health.toInt()
                        getBinding().sbMoney.progress=it.data.money.toInt()
                        getBinding().sbWork.progress=it.data.work.toInt()
                        getBinding().sbAll.progress=it.data.all.toInt()

                        getBinding().tvColor.text="幸运颜色:${it.data.color}"
                        getBinding().tvDate.text="当前时间:${it.data.datetime}"
                        getBinding().tvName.text="星座名称:${it.data.name}"
                        getBinding().tvFriend.text="速配星座:${it.data.friend}"
                        getBinding().tvSummary.text="今日概述:${it.data.summary}"
                        getBinding().tvNumber.text="幸运数字:${it.data.number}"
                    }
                }
            }

            override fun onFailure(p0: Call<TodayData>, p1: Throwable) {

            }
        })
    }

    override fun initView() {

    }

    companion object {
        @JvmStatic
        fun newInstance(cid: String) =
            TodayFragment().apply {
                arguments = Bundle().apply {
                    putString("cid", cid)
                }
            }
    }
}