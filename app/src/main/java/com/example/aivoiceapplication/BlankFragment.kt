package com.example.aivoiceapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aivoiceapplication.databinding.FragmentBlankBinding
import com.example.lib_base.base.BaseFragment

class BlankFragment : BaseFragment<FragmentBlankBinding>() {
    override fun initViewBinding(inflater: LayoutInflater): FragmentBlankBinding {
        return FragmentBlankBinding.inflate(inflater)
    }

    override fun initView() {

    }
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}