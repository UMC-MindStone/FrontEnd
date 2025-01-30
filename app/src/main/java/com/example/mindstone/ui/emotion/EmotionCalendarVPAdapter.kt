package com.example.mindstone.ui.emotion

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class EmotionCalendarVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun updateFragment(position: Int, fragment: Fragment) {
        if (position < 0 || position >= fragmentList.size) return
        fragmentList[position] = fragment
        notifyItemChanged(position)
    }
}

