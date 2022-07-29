package com.example.tutor.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tutor.adapters.StatisticVPAdapter
import com.example.tutor.databinding.FragmentStatisticPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class StatisticPagerFragment : Fragment() {
   lateinit var binding: FragmentStatisticPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStatisticPagerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPagerAndTab()
    }

    fun setPagerAndTab() {
        binding.statisticViewPager.adapter = StatisticVPAdapter(this)// подключл адаптер пейджера
        // подключил TabLayout
        TabLayoutMediator(binding.statisticTabLayout, binding.statisticViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText("Доход")
                }
                else -> tab.setText("Занятия")
            }
        }.attach()
    }

}