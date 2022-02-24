package com.example.sandeep.app_sharing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdaptor(supportfrag :FragmentManager): FragmentStatePagerAdapter(supportfrag,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragementList=ArrayList<Fragment>()

    override fun getCount(): Int {
        return fragementList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragementList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }

    fun addFragment(frg:Fragment){
        fragementList.add(frg)

    }
}