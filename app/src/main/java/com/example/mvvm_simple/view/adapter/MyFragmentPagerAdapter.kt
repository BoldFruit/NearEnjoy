package com.example.mvvm_simple.view.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author DuLong
 * @since 2020/4/8
 * email 798382030@qq.com
 */

class MyFragmentPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>, private val values: ArrayList<String>): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
       val data: String = values[position]
        if (data.length > 2) {
            return data.substring(0, 2)
        }
        return data;
    }

}