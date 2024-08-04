package com.example.module_constellation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.base.BaseFragment
import com.example.lib_network.HttpManager
import com.example.lib_network.bean.constell.YearData
import com.example.module_constellation.R
import com.example.module_constellation.databinding.FragmentYearBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class YearFragment : BaseFragment<FragmentYearBinding>() {
    // TODO: Rename and change types of parameters
    private var cid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cid = it.getString(ARG_PARAM1)
        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentYearBinding {
        return FragmentYearBinding.inflate(inflater)
    }

    override fun intData() {
        HttpManager.queryThisYearConstellation(cid!!, object : Callback<YearData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(p0: Call<YearData>, p1: Response<YearData>) {
                p1.body()?.let {
                    if (it.code==1) {
                        getBinding().tvDate.text="当前时间：${it.data.date}"
                        getBinding().tvName.text="星座名称：${it.data.name}"
                        getBinding().tvLove.text="感情运：${it.data.love}"
                        getBinding().tvCareer.text="事业运：${it.data.career}"
                        getBinding().tvFinance.text="财运：${it.data.finance}"
                        getBinding().tvHealth.text="健康运：${it.data.health}"
                        getBinding().tvMimaInfo.text="年度密码：${it.data.mima_text}"
                        getBinding().tvLuckeySton.text="幸运石：${it.data.luckey_stone}"
                        getBinding().tvMimaText.text="密码解读：${it.data.mima_info}"
                    }

                }
            }

            override fun onFailure(p0: Call<YearData>, p1: Throwable) {
            }
        })
    }

    override fun initView() {
        Log.d("YearFragment", "cid: $cid")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            YearFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}