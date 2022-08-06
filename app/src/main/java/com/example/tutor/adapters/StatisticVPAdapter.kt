package com.example.tutor.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tutor.statistic.lessons.LessonsStatisticFragment
import com.example.tutor.statistic.amount.AmountStatisticFragment

class StatisticVPAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0 -> AmountStatisticFragment()
           else -> LessonsStatisticFragment()
       }
    }

}