package com.example.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @description: TODO 父类Fragment
 * @author: mlf
 * @date: 2024/6/24 14:56
 * @version: 1.0
 */
abstract class BaseFragment<T : ViewBinding>: androidx.fragment.app.Fragment() {
    private lateinit var _binding: T;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= initViewBinding(inflater)
        return _binding.root
    }
    fun getBinding(): T {
        return _binding
    }

    abstract fun initViewBinding(inflater: LayoutInflater): T


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()
}