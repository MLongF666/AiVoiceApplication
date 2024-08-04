package com.example.module_constellation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @description: TODO fragmentviewpager适配器
 * @author: mlf
 * @date: 2024/8/1 11:26
 * @version: 1.0
 */
class FragmentAdapter(lifecycle: Lifecycle, fragmentManager: FragmentManager,
                      private val fragments: List<Fragment>
)
    : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}