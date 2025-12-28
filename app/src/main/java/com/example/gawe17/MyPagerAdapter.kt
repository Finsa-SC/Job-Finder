package com.example.gawe17

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gawe17.explore.ExploreFragment
import com.example.gawe17.myjob.MyJobFragment
import com.example.gawe17.profile.ProfileFragment

class MyPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ExploreFragment()
            1 -> MyJobFragment()
            2 -> ProfileFragment()
            else -> ExploreFragment()
        }
    }
}
