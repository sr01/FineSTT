package com.rosi.masts.mvc.view.android.activity.keybinding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class KeyBindingFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            SELECT_ACTION_FRAGMENT_INDEX -> SelectActionFragment()
            SELECT_KEY_FRAGMENT_INDEX -> SelectKeyFragment()
            else -> throw Exception("invalid fragment position $position")
        }
    }

    companion object {
        const val NUM_PAGES = 2
        const val SELECT_ACTION_FRAGMENT_INDEX = 0
        const val SELECT_KEY_FRAGMENT_INDEX = 1
    }
}