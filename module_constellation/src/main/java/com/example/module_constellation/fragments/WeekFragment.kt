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
import com.example.lib_network.bean.constell.WeekData
import com.example.module_constellation.R
import com.example.module_constellation.databinding.FragmentWeekBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "cid"

/**
 * A simple [Fragment] subclass.
 * Use the [WeekFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeekFragment : BaseFragment<FragmentWeekBinding>() {
    // TODO: Rename and change types of parameters
    private var cid: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cid = it.getString(ARG_PARAM1)
        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentWeekBinding {
        return FragmentWeekBinding.inflate(inflater)
    }

    override fun intData() {
        HttpManager.queryThisWeekConstellation(cid!!, object : Callback<WeekData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(p0: Call<WeekData>, p1: Response<WeekData>) {
                p1.body()?.let {
                    getBinding().tvDate.text="当前时间：${it.data.date}"
                    getBinding().tvName.text="星座名称：${it.data.name}"
                    getBinding().tvWeekth.text="第${it.data.weekth}周"
                    getBinding().tvHealth.text="健康：${it.data.health}"
                    getBinding().tvLove.text="爱情：${it.data.love}"
                    getBinding().tvMoney.text="财运：${it.data.money}"
                    getBinding().tvWork.text="工作：${it.data.work}"
                }
            }

            override fun onFailure(p0: Call<WeekData>, p1: Throwable) {

            }

        })
    }

    override fun initView() {
        Log.d("WeekFragment", "cid: $cid")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            WeekFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}