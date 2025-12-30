package com.example.gawe17.myjob

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewAdapterJob(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FavoriteFragment()
            1-> ApplicationListFragment()
            else -> FavoriteFragment()
        }
    }
}