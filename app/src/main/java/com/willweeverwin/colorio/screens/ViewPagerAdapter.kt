package com.willweeverwin.colorio.screens

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    private val fragments: List<Fragment>,
    fm: FragmentManager,
    lc: Lifecycle
): FragmentStateAdapter(fm, lc) {
    override fun getItemCount() = fragments.size

    override fun createFragment(pos: Int) = fragments[pos]
}