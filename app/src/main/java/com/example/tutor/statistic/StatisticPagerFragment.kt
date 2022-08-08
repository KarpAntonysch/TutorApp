package com.example.tutor.statistic

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tutor.R
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.statisticToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Статистика"
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
    // меню ToolBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.pages_tool_bar, menu)
    }

    // слушатель айтемов меню ToolBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info -> {
                var toast = Toast.makeText(requireContext(), "статиска" , Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

}