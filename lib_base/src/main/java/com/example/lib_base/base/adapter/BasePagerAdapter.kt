package com.example.lib_base.base.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager



class BasePagerAdapter(private val mList: List<View>) : PagerAdapter() {
    private val TAG = "BasePagerAdapter"
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val currentPosition: Int = (position % mList.size)
        val iv: View = mList[currentPosition]
        view.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        Log.e(TAG, "destroyItem")
//        super.destroyItem(container, position, `object`)
        container.removeView(mList[position])
    }
}