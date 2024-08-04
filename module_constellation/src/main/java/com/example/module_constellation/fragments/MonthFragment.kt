package com.example.module_constellation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.base.BaseFragment
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.constell.MonthData
import com.example.module_constellation.R
import com.example.module_constellation.databinding.FragmentMonthBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "cid"
class MonthFragment : BaseFragment<FragmentMonthBinding>() {
    // TODO: Rename and change types of parameters
    private var cid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cid = it.getString(ARG_PARAM1)
        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentMonthBinding {
        return FragmentMonthBinding.inflate(inflater)
    }

    override fun intData() {
        HttpManager.queryThisMonthConstellation(cid!!, object : Callback<MonthData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(p0: Call<MonthData>, p1: Response<MonthData>) {
                p1.body()?.let {
                    if (it.code == 1) {
                        getBinding().tvDate.text="当前时间：${it.data.date}"
                        getBinding().tvName.text="星座名称：${it.data.name}"
                        getBinding().tvLove.text="爱情：${it.data.love}"
                        getBinding().tvMoney.text="财运：${it.data.money}"
                        getBinding().tvHealth.text="健康：${it.data.health}"
                        getBinding().tvWork.text="工作：${it.data.work}"
                    }
                }
            }

            override fun onFailure(p0: Call<MonthData>, p1: Throwable) {

            }
        })
    }

    override fun initView() {

    }

    companion object {
        @JvmStatic
        fun newInstance(cid: String) =
            MonthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, cid)
                }
            }
    }
}