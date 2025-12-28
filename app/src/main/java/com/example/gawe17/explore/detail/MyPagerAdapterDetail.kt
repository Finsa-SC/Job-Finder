package com.example.gawe17.explore.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapterDetail(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> CompanyFragment()
            1-> JobDetailFragment()
            else -> CompanyFragment()
        }
    }
}