package com.slc.amarn.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.slc.amarn.views.ProfileFragment

class PagerAdapter (manager: FragmentManager): FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> ProfileFragment()
            1 -> ProfileFragment()
            else -> ProfileFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "One"
            1 -> "Two"
            else -> "Three"
        }
    }
}